package cl.flepe.payment.presentation

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.factory.PaymentFactory.makeCardIssuersList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCardIssuerResponseList
import cl.flepe.payment.presentation.cardissuers.CardIssuersProcessor
import cl.flepe.payment.presentation.cardissuers.CardIssuersReducer
import cl.flepe.payment.presentation.cardissuers.CardIssuersUIntent
import cl.flepe.payment.presentation.cardissuers.CardIssuersUiState
import cl.flepe.payment.presentation.cardissuers.mapper.CardIssuersMapper
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketTimeoutException

@FlowPreview
@ExperimentalCoroutinesApi
internal class CardIssuersViewModelTest {

    private val reducer = CardIssuersReducer()
    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<CardIssuersMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = CardIssuersProcessor(
        repository,
        mapper,
        executionThread
    )
    private val viewModel = CardIssuersViewModel(reducer, processor)

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val paymentMethodId = generateString()
            val userIntent = CardIssuersUIntent.InitialUIntent(paymentMethodId)
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            val cardIssuers = makeCardIssuersList(2)
            stubGetCardIssuersRemote(paymentMethodId, remoteCardIssuerResponse)
            stubCardIssuersMapper(remoteCardIssuerResponse, cardIssuers)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            Assert.assertTrue(stateFlow.first() is CardIssuersUiState.DefaultUiState)
            Assert.assertTrue(stateFlow[1] is CardIssuersUiState.LoadingUiState)
            Assert.assertTrue(stateFlow.last() is CardIssuersUiState.DisplayCardIssuersUiState)
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then DisplayPaymentMethodsUiState and data`() =
        runBlocking {
            val paymentMethodId = generateString()
            val userIntent = CardIssuersUIntent.InitialUIntent(paymentMethodId)
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            val cardIssuers = makeCardIssuersList(2)
            stubGetCardIssuersRemote(paymentMethodId, remoteCardIssuerResponse)
            stubCardIssuersMapper(remoteCardIssuerResponse, cardIssuers)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertEquals(
                cardIssuers,
                (stateFlow.last() as CardIssuersUiState.DisplayCardIssuersUiState).cardIssuers
            )
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for failure`() =
        runBlocking {
            val paymentMethodId = generateString()
            val userIntent = CardIssuersUIntent.InitialUIntent(paymentMethodId)
            stubGetCardIssuersRemoteError(paymentMethodId)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            Assert.assertTrue(stateFlow.first() is CardIssuersUiState.DefaultUiState)
            Assert.assertTrue(stateFlow[1] is CardIssuersUiState.LoadingUiState)
            Assert.assertTrue(stateFlow.last() is CardIssuersUiState.ErrorUiState)
        }

    @Test
    fun `given RetrySeeCardIssuers, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val paymentMethodId = generateString()
            val userIntent = CardIssuersUIntent.RetrySeeCardIssuers(paymentMethodId)
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            val cardIssuers = makeCardIssuersList(2)
            stubGetCardIssuersRemote(paymentMethodId, remoteCardIssuerResponse)
            stubCardIssuersMapper(remoteCardIssuerResponse, cardIssuers)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            Assert.assertTrue(stateFlow.first() is CardIssuersUiState.DefaultUiState)
            Assert.assertTrue(stateFlow[1] is CardIssuersUiState.LoadingUiState)
            Assert.assertTrue(stateFlow.last() is CardIssuersUiState.DisplayCardIssuersUiState)
        }

    private fun stubGetCardIssuersRemoteError(paymentMethodId: String) {
        coEvery { repository.getCardIssuers(paymentMethodId) } returns flow { throw SocketTimeoutException("error") }
    }

    private fun stubGetCardIssuersRemote(
        paymentMethodId: String,
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>
    ) {
        coEvery { repository.getCardIssuers(paymentMethodId) } returns flow {
            emit(
                remoteCardIssuerResponse
            )
        }
    }

    private fun stubCardIssuersMapper(
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>,
        cardIssuer: List<CardIssuer>
    ) {
        every {
            remoteCardIssuerResponse.flatMap { remoteCardIssuer ->
                with(mapper) {
                    remoteCardIssuer.toPresentation()
                }
            }
        } returns cardIssuer
    }
}

package cl.flepe.payment.presentation

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.factory.PaymentFactory.makeCreditCardList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCreditCardResponseList
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsProcessor
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsReducer
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.InitialUIntent
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.RetrySeePaymentMethods
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState.*
import cl.flepe.payment.presentation.paymentmethods.mapper.PaymentMethodsMapper
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.SocketTimeoutException


@FlowPreview
@ExperimentalCoroutinesApi
internal class PaymentMethodsViewModelTest {

    private val reducer = PaymentMethodsReducer()
    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<PaymentMethodsMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = PaymentMethodsProcessor(
        repository,
        mapper,
        executionThread
    )
    private val viewModel = PaymentMethodsViewModel(reducer, processor)

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val userIntent = InitialUIntent
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(2)
            val creditCards = makeCreditCardList(2)
            stubGetPaymentMethodsRemote(remoteCreditCardResponse)
            stubPaymentMethodsMapper(remoteCreditCardResponse, creditCards)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is DisplayPaymentMethodsUiState)
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then DisplayCardsUiState and data`() =
        runBlocking {
            val userIntent = InitialUIntent
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(2)
            val creditCards = makeCreditCardList(2)
            stubGetPaymentMethodsRemote(remoteCreditCardResponse)
            stubPaymentMethodsMapper(remoteCreditCardResponse, creditCards)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertEquals(creditCards, (stateFlow.last() as DisplayPaymentMethodsUiState).creditCard)
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for failure`() =
        runBlocking {
            val userIntent = InitialUIntent
            stubGetPaymentMethodsRemoteError()

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is ErrorUiState)
        }

    @Test
    fun `given RetrySeePaymentMethods, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val userIntent = RetrySeePaymentMethods
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(2)
            val creditCards = makeCreditCardList(2)
            stubGetPaymentMethodsRemote(remoteCreditCardResponse)
            stubPaymentMethodsMapper(remoteCreditCardResponse, creditCards)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is DisplayPaymentMethodsUiState)
        }

    private fun stubGetPaymentMethodsRemoteError() {
        coEvery { repository.getPaymentMethods() } returns flow { throw SocketTimeoutException("error") }
    }

    private fun stubGetPaymentMethodsRemote(remoteCreditCardResponse: List<RemoteCreditCardResponse>) {
        coEvery { repository.getPaymentMethods() } returns flow { emit(remoteCreditCardResponse) }
    }

    private fun stubPaymentMethodsMapper(
        remoteCreditCardResponse: List<RemoteCreditCardResponse>,
        creditCard: List<CreditCard>
    ) {
        every {
            remoteCreditCardResponse.flatMap { remoteCreditCard ->
                with(mapper) {
                    remoteCreditCard.toPresentation()
                }
            }
        } returns creditCard
    }
}
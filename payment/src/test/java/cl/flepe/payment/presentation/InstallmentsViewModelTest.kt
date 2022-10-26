package cl.flepe.payment.presentation

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.factory.GenerateValues.generateInt
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.factory.PaymentFactory.makePayerCostsResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponseList
import cl.flepe.payment.presentation.installments.InstallmentsProcessor
import cl.flepe.payment.presentation.installments.InstallmentsReducer
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.InitialUIntent
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.RetrySeeInstallments
import cl.flepe.payment.presentation.installments.InstallmentsUiState.*
import cl.flepe.payment.presentation.installments.mapper.InstallmentsMapper
import cl.flepe.payment.presentation.installments.model.PayerCost
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
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.SocketTimeoutException

@FlowPreview
@ExperimentalCoroutinesApi
internal class InstallmentsViewModelTest {

    private val reducer = InstallmentsReducer()
    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<InstallmentsMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = InstallmentsProcessor(
        repository,
        mapper,
        executionThread
    )
    private val viewModel = InstallmentsViewModel(reducer, processor)

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            val userIntent = InitialUIntent(amount, paymentMethodId, issuerId)
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            val payerCosts = makePayerCostsResponseList(2)
            stubGetInstallmentsRemote(amount, paymentMethodId, issuerId, remotePayerCostsResponse)
            stubInstallmentsMapper(remotePayerCostsResponse, payerCosts)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is DisplayInstallmentsUiState)
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then DisplayInstallmentsUiState and data`() =
        runBlocking {
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            val userIntent = InitialUIntent(amount, paymentMethodId, issuerId)
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            val payerCosts = makePayerCostsResponseList(2)
            stubGetInstallmentsRemote(amount, paymentMethodId, issuerId, remotePayerCostsResponse)
            stubInstallmentsMapper(remotePayerCostsResponse, payerCosts)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            Assert.assertEquals(
                payerCosts,
                (stateFlow.last() as DisplayInstallmentsUiState).payerCosts
            )
        }

    @Test
    fun `given InitialUIntent, when processUserIntents, then UiStates for failure`() =
        runBlocking {
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            val userIntent = InitialUIntent(amount, paymentMethodId, issuerId)
            stubGetInstallmentsRemoteError(amount, paymentMethodId, issuerId)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is ErrorUiState)
        }

    @Test
    fun `given RetrySeeInstallments, when processUserIntents, then UiStates for display`() =
        runBlocking {
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            val userIntent = RetrySeeInstallments(amount, paymentMethodId, issuerId)
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            val payerCosts = makePayerCostsResponseList(2)
            stubGetInstallmentsRemote(amount, paymentMethodId, issuerId, remotePayerCostsResponse)
            stubInstallmentsMapper(remotePayerCostsResponse, payerCosts)

            val stateFlow =
                viewModel.processUserIntentsAndObserveUiStates(flow { emit(userIntent) }).take(3)
                    .toList()

            assertTrue(stateFlow.first() is DefaultUiState)
            assertTrue(stateFlow[1] is LoadingUiState)
            assertTrue(stateFlow.last() is DisplayInstallmentsUiState)
        }

    private fun stubGetInstallmentsRemoteError(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ) {
        coEvery { repository.getInstallments(amount, paymentMethodId, issuerId) } returns flow {
            throw SocketTimeoutException(
                "error"
            )
        }
    }

    private fun stubGetInstallmentsRemote(
        amount: Int,
        paymentMethodId: String,
        issuerId: String,
        remotePayerCostsResponse: List<RemotePayerCostsResponse>
    ) {
        coEvery { repository.getInstallments(amount, paymentMethodId, issuerId) } returns flow {
            emit(remotePayerCostsResponse)
        }
    }

    private fun stubInstallmentsMapper(
        remotePayerCostsResponse: List<RemotePayerCostsResponse>,
        payerCost: List<PayerCost>
    ) {
        every {
            remotePayerCostsResponse.flatMap { remotePayerCost ->
                with(mapper) {
                    remotePayerCost.toPresentation()
                }
            }
        } returns payerCost
    }
}

package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.factory.GenerateValues.generateInt
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.factory.PaymentFactory.makePayerCostsResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponseList
import cl.flepe.payment.presentation.installments.InstallmentsAction.GetInstallmentsAction
import cl.flepe.payment.presentation.installments.mapper.InstallmentsMapper
import cl.flepe.payment.presentation.installments.model.PayerCost
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class InstallmentsProcessorTest {

    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<InstallmentsMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = InstallmentsProcessor(repository, mapper, executionThread)

    @Test
    fun `given GetInstallmentsAction remote ok, when call 'actionProcessor', then return Result-Success`() =
        runBlocking {
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            val payerCosts = makePayerCostsResponseList(2)
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            stubInstallmentsMapper(remotePayerCostsResponse, payerCosts)
            stubGetRemoteInstallments(amount, paymentMethodId, issuerId, remotePayerCostsResponse)

            val results =
                processor.actionProcessor(
                    GetInstallmentsAction(amount, paymentMethodId, issuerId)
                ).toList()

            assertEquals(results[0], InstallmentsResult.GetInstallmentsResult.InProgress)
            assertEquals(
                results[1], InstallmentsResult.GetInstallmentsResult.Success(payerCosts)
            )
        }

    @Test
    fun `given GetInstallmentsAction and remote nok, when call 'actionProcessor', then return Result-Error`() =
        runBlocking {
            val error = Throwable()
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            val payerCosts = makePayerCostsResponseList(2)
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            stubInstallmentsMapper(remotePayerCostsResponse, payerCosts)
            stubGetRemoteInstallmentsError(amount, paymentMethodId, issuerId, error)

            val results =
                processor.actionProcessor(
                    GetInstallmentsAction(amount, paymentMethodId, issuerId)
                ).toList()

            assertEquals(results[0], InstallmentsResult.GetInstallmentsResult.InProgress)
            assertEquals(results[1], InstallmentsResult.GetInstallmentsResult.Error)
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

    private fun stubGetRemoteInstallments(
        amount: Int,
        paymentMethodId: String,
        issuerId: String,
        remotePayerCostResponse: List<RemotePayerCostsResponse>
    ) {
        coEvery { repository.getInstallments(amount, paymentMethodId, issuerId) } returns flow {
            emit(remotePayerCostResponse)
        }
    }

    private fun stubGetRemoteInstallmentsError(
        amount: Int,
        paymentMethodId: String,
        issuerId: String, error: Throwable
    ) {
        coEvery {
            repository.getInstallments(
                amount,
                paymentMethodId,
                issuerId
            )
        } returns flow { throw error }
    }
}

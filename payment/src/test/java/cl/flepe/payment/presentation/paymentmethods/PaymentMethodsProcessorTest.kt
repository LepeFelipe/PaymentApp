package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.factory.PaymentFactory.makeCreditCardList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCreditCardResponseList
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsResult.GetPaymentMethodsResult
import cl.flepe.payment.presentation.paymentmethods.mapper.PaymentMethodsMapper
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class PaymentMethodsProcessorTest {

    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<PaymentMethodsMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = PaymentMethodsProcessor(repository, mapper, executionThread)

    @Test
    fun `given GetPaymentMethodsAction remote ok, when call 'actionProcessor', then return Result-Success`() =
        runBlocking {
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(2)
            val creditCards = makeCreditCardList(2)
            stubPaymentMethodsMapper(remoteCreditCardResponse, creditCards)
            stubGetRemotePaymentMethods(remoteCreditCardResponse)

            val results =
                processor.actionProcessor(
                    PaymentMethodsAction.GetPaymentMethodsAction
                ).toList()

            assertEquals(results[0], GetPaymentMethodsResult.InProgress)
            assertEquals(results[1], GetPaymentMethodsResult.Success(creditCards))
        }

    @Test
    fun `given GetPaymentMethodsAction and remote nok, when call 'actionProcessor', then return Result-Error`() =
        runBlocking {
            val error = Throwable()
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(2)
            val creditCards = makeCreditCardList(2)
            stubPaymentMethodsMapper(remoteCreditCardResponse, creditCards)
            stubGetRemotePaymentMethodsError(error)

            val results =
                processor.actionProcessor(
                    PaymentMethodsAction.GetPaymentMethodsAction
                ).toList()

            assertEquals(results[0], GetPaymentMethodsResult.InProgress)
            assertEquals(results[1], GetPaymentMethodsResult.Error)
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

    private fun stubGetRemotePaymentMethods(remoteCreditCardResponse: List<RemoteCreditCardResponse>) {
        coEvery { repository.getPaymentMethods() } returns flow { emit(remoteCreditCardResponse) }
    }

    private fun stubGetRemotePaymentMethodsError(error: Throwable) {
        coEvery { repository.getPaymentMethods() } returns flow { throw error }
    }
}

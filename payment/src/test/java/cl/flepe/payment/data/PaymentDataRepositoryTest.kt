package cl.flepe.payment.data

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.data.source.PaymentRemote
import cl.flepe.payment.factory.GenerateValues
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCardIssuerResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCreditCardResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
internal class PaymentDataRepositoryTest {

    private val remote = mockk<PaymentRemote>()

    private val repository = PaymentDataRepository(remote)

    @Test
    fun `when call getPaymentMethods, return as RemoteCreditCardResponse`() =
        runBlocking {
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(4)
            stubGetPaymentMethodsInRemote(remoteCreditCardResponse)

            val flow = repository.getPaymentMethods()

            flow.collect { result -> assertEquals(remoteCreditCardResponse, result) }
            coVerify { remote.getPaymentMethods() }
        }

    @Test
    fun `when call getCardIssuers, return as RemoteCardIssuerResponse`() =
        runBlocking {
            val paymentMethodId = GenerateValues.generateString()
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            stubGetCardIssuersInRemote(paymentMethodId, remoteCardIssuerResponse)

            val flow = repository.getCardIssuers(paymentMethodId = paymentMethodId)

            flow.collect { result -> assertEquals(remoteCardIssuerResponse, result) }
            coVerify { remote.getCardIssuers(paymentMethodId = paymentMethodId) }
        }

    @Test
    fun `when call getInstallments, return as RemotePayerCostsResponse`() =
        runBlocking {
            val amount = GenerateValues.generateInt()
            val paymentMethodId = GenerateValues.generateString()
            val issuerId = GenerateValues.generateString()
            val remotePayerCostsResponse = makeRemotePayerCostsResponse(5)
            stubGetInstallmentsInRemote(
                amount,
                paymentMethodId,
                issuerId,
                remotePayerCostsResponse
            )

            val flow = repository.getInstallments(
                amount = amount,
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )

            flow.collect { result -> assertEquals(remotePayerCostsResponse, result) }
            coVerify {
                remote.getInstallments(
                    amount = amount,
                    paymentMethodId = paymentMethodId,
                    issuerId = issuerId
                )
            }
        }

    private fun stubGetPaymentMethodsInRemote(remoteCreditCardResponse: List<RemoteCreditCardResponse>) {
        coEvery { remote.getPaymentMethods() } returns remoteCreditCardResponse
    }

    private fun stubGetCardIssuersInRemote(
        paymentMethodId: String,
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>
    ) {
        coEvery { remote.getCardIssuers(paymentMethodId = paymentMethodId) } returns remoteCardIssuerResponse
    }

    private fun stubGetInstallmentsInRemote(
        amount: Int,
        paymentMethodId: String,
        issuerId: String,
        remotePayerCostsResponse: RemotePayerCostsResponse
    ) {
        coEvery {
            remote.getInstallments(
                amount = amount,
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )
        } returns remotePayerCostsResponse
    }
}

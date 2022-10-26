package cl.flepe.payment.data.remote

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.data.remote.retrofit.PaymentWebService
import cl.flepe.payment.factory.GenerateValues.generateInt
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCardIssuerResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCreditCardResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponse
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponseList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PaymentRemoteImplTest {

    private val webService = mockk<PaymentWebService>()

    private val remoteImpl = PaymentRemoteImpl(webService)

    @Test
    fun `when call getPaymentMethods, then return a list of RemoteCreditCardResponse`() =
        runBlocking {
            val remoteCreditCardResponse = makeRemoteCreditCardResponseList(3)
            stubGetPaymentMethodsInWebService(remoteCreditCardResponse)

            val result = remoteImpl.getPaymentMethods()

            assertEquals(remoteCreditCardResponse, result)
            coVerify { webService.getPaymentMethods() }
        }

    @Test
    fun `when call getCardIssuers, then return a list of RemoteCardIssuerResponse`() = runBlocking {
        val paymentMethodId = generateString()
        val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(3)
        stubGetCardIssuersInWebService(paymentMethodId, remoteCardIssuerResponse)

        val result = remoteImpl.getCardIssuers(paymentMethodId)

        assertEquals(remoteCardIssuerResponse, result)
        coVerify { webService.getCardIssuers(paymentMethodId = paymentMethodId) }
    }

    @Test
    fun `when call getInstallments, then return RemotePayerCostsResponse`() =
        runBlocking {
            val amount = generateInt()
            val paymentMethodId = generateString()
            val issuerId = generateString()
            val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)
            stubGetInstallmentsInWebService(
                amount,
                paymentMethodId,
                issuerId,
                remotePayerCostsResponse
            )

            val result = remoteImpl.getInstallments(
                amount = amount,
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )

            assertEquals(remotePayerCostsResponse, result)
            coVerify {
                webService.getInstallments(
                    amount = amount,
                    paymentMethodId = paymentMethodId,
                    issuerId = issuerId
                )
            }
        }

    private fun stubGetPaymentMethodsInWebService(remoteCreditCardResponse: List<RemoteCreditCardResponse>) {
        coEvery { webService.getPaymentMethods() } returns remoteCreditCardResponse
    }

    private fun stubGetCardIssuersInWebService(
        paymentMethodId: String,
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>
    ) {
        coEvery { webService.getCardIssuers(paymentMethodId = paymentMethodId) } returns remoteCardIssuerResponse
    }

    private fun stubGetInstallmentsInWebService(
        amount: Int,
        paymentMethodId: String,
        issuerId: String,
        remotePayerCostsResponse: List<RemotePayerCostsResponse>
    ) {
        coEvery {
            webService.getInstallments(
                amount = amount,
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )
        } returns remotePayerCostsResponse
    }
}

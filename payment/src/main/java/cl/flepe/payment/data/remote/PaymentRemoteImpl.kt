package cl.flepe.payment.data.remote

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.data.remote.retrofit.PaymentWebService
import cl.flepe.payment.data.source.PaymentRemote
import javax.inject.Inject

internal class PaymentRemoteImpl @Inject constructor(private val webService: PaymentWebService) :
    PaymentRemote {
    override suspend fun getPaymentMethods(): List<RemoteCreditCardResponse> =
        webService.getPaymentMethods()


    override suspend fun getCardIssuers(paymentMethodId: String): List<RemoteCardIssuerResponse> =
        webService.getCardIssuers(paymentMethodId = paymentMethodId)

    override suspend fun getInstallments(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): List<RemotePayerCostsResponse> = webService.getInstallments(
        amount = amount,
        paymentMethodId = paymentMethodId,
        issuerId = issuerId
    )

}
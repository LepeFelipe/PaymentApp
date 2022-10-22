package cl.flepe.payment.data.source

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse

internal interface PaymentRemote {
    suspend fun getPaymentMethods(): List<RemoteCreditCardResponse>
    suspend fun getCardIssuers(paymentMethodId: String): List<RemoteCardIssuerResponse>
    suspend fun getInstallments(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): RemotePayerCostsResponse
}
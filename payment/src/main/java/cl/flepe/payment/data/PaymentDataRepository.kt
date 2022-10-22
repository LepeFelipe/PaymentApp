package cl.flepe.payment.data

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.data.source.PaymentRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class PaymentDataRepository @Inject constructor(private val remote: PaymentRemote) {
    fun getPaymentMethods(): Flow<List<RemoteCreditCardResponse>> = flow {
        val creditCards = remote.getPaymentMethods()
        emit(creditCards)
    }

    fun getCardIssuers(paymentMethodId: String): Flow<List<RemoteCardIssuerResponse>> = flow {
        val cardIssuers = remote.getCardIssuers(paymentMethodId)
        emit(cardIssuers)
    }

    fun getInstallments(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): Flow<RemotePayerCostsResponse> = flow {
        val installments = remote.getInstallments(amount, paymentMethodId, issuerId)
        emit(installments)
    }
}

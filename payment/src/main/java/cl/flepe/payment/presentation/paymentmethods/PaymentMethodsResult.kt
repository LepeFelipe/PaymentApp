package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviResult
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard

internal sealed class PaymentMethodsResult : MviResult {
    sealed class GetPaymentMethodsResult : PaymentMethodsResult() {
        object InProgress : GetPaymentMethodsResult()
        data class Success(val creditCards: List<CreditCard>) : GetPaymentMethodsResult()
        object Error : GetPaymentMethodsResult()
    }
}

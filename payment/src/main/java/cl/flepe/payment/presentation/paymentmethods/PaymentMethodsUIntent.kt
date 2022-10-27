package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviUserIntent

internal sealed class PaymentMethodsUIntent : MviUserIntent {
    object InitialUIntent : PaymentMethodsUIntent()
    object RetrySeePaymentMethods : PaymentMethodsUIntent()
}

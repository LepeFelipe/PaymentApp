package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviAction

internal sealed class PaymentMethodsAction : MviAction {
    object GetPaymentMethodsAction : PaymentMethodsAction()
}

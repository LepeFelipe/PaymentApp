package cl.flepe.payment.ui.provider

import cl.flepe.payment_components.components.AttrsGenericErrorComponent

interface PaymentProvider {
    fun forGenericError(action: () -> Unit): AttrsGenericErrorComponent
}
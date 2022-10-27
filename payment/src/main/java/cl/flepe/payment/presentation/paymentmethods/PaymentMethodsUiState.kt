package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviUiState
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard

internal sealed class PaymentMethodsUiState : MviUiState {
    object DefaultUiState : PaymentMethodsUiState()
    object LoadingUiState : PaymentMethodsUiState()
    data class DisplayPaymentMethodsUiState(val creditCard: List<CreditCard>) : PaymentMethodsUiState()
    object ErrorUiState : PaymentMethodsUiState()
}

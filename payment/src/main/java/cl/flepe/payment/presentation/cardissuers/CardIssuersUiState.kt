package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.MviUiState
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard

internal sealed class CardIssuersUiState : MviUiState {
    object DefaultUiState : CardIssuersUiState()
    object LoadingUiState : CardIssuersUiState()
    data class DisplayCardIssuersUiState(val cardIssuers: List<CardIssuer>) : CardIssuersUiState()
    object ErrorUiState : CardIssuersUiState()
}

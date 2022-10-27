package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.MviUiState
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.presentation.installments.model.Installment
import cl.flepe.payment.presentation.installments.model.PayerCost

internal sealed class InstallmentsUiState : MviUiState {
    object DefaultUiState : InstallmentsUiState()
    object LoadingUiState : InstallmentsUiState()
    data class DisplayInstallmentsUiState(val payerCosts: List<PayerCost>) : InstallmentsUiState()
    object ErrorUiState : InstallmentsUiState()
}

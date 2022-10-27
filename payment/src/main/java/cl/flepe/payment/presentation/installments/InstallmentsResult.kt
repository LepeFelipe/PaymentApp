package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.MviResult
import cl.flepe.payment.presentation.installments.model.Installment
import cl.flepe.payment.presentation.installments.model.PayerCost

internal sealed class InstallmentsResult : MviResult {
    sealed class GetInstallmentsResult : InstallmentsResult() {
        object InProgress : GetInstallmentsResult()
        data class Success(val payerCosts: List<PayerCost>) : GetInstallmentsResult()
        object Error : GetInstallmentsResult()
    }
}

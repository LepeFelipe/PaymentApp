package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.MviAction

internal sealed class InstallmentsAction : MviAction {
    data class GetInstallmentsAction(
        val amount: Int,
        val paymentMethodId: String,
        val issuerId: String
    ) : InstallmentsAction()
}

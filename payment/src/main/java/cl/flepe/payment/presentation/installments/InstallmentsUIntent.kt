package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.MviUserIntent

internal sealed class InstallmentsUIntent : MviUserIntent {
    data class InitialUIntent(
        val amount: Int,
        val paymentMethodId: String,
        val issuerId: String
    ) : InstallmentsUIntent()

    data class RetrySeeInstallments(
        val amount: Int,
        val paymentMethodId: String,
        val issuerId: String
    ) : InstallmentsUIntent()
}

package cl.flepe.payment.presentation.installments.mapper

import cl.flepe.payment.data.remote.model.RemoteInstallments
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.presentation.installments.model.Installment
import cl.flepe.payment.presentation.installments.model.PayerCost
import javax.inject.Inject

internal class InstallmentsMapper @Inject constructor() {
    fun RemotePayerCostsResponse.toPresentation(): List<PayerCost> {
        val result = mutableListOf<PayerCost>()
        result.add(
            PayerCost(
                installments = installments?.map { it.toPresentation() }.orEmpty()
            )
        )
        return result
    }

    fun RemoteInstallments.toPresentation(): Installment {
        return Installment(message = recommended_message.orEmpty())
    }
}

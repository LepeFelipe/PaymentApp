package cl.flepe.payment.ui.installments.mapper

import cl.flepe.payment.presentation.installments.model.Installment
import cl.flepe.payment.ui.customSpinner.adapter.AttrsCustomSpinner
import javax.inject.Inject

class UiInstallmentsMapper @Inject constructor() {

    fun Installment.toAttrsCustomSpinner(): List<AttrsCustomSpinner> {
        val result = mutableListOf<AttrsCustomSpinner>()
        result.add(
            AttrsCustomSpinner(
                id = "",
                name = message,
                icon = null
            )
        )
        return result
    }
}
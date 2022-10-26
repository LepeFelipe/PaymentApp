package cl.flepe.payment.ui.paymentmethods.mapper

import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import cl.flepe.payment.ui.customSpinner.adapter.AttrsCustomSpinner
import javax.inject.Inject

class CreditCardMapper @Inject constructor() {
    fun CreditCard.toAttrsCustomSpinner(): List<AttrsCustomSpinner> {
        val result = mutableListOf<AttrsCustomSpinner>()
        result.add(
            AttrsCustomSpinner(
                id = cardId,
                name = name,
                icon = thumbnail
            )
        )
        return result
    }
}

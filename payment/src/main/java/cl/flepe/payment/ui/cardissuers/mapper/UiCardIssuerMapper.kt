package cl.flepe.payment.ui.cardissuers.mapper

import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.ui.customSpinner.adapter.AttrsCustomSpinner
import javax.inject.Inject

class UiCardIssuerMapper @Inject constructor() {
    fun CardIssuer.toAttrsCustomSpinner(): List<AttrsCustomSpinner> {
        val result = mutableListOf<AttrsCustomSpinner>()
        result.add(
            AttrsCustomSpinner(
                id = cardIssuerId,
                name = name,
                icon = thumbnail
            )
        )
        return result
    }
}
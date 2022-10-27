package cl.flepe.payment.presentation.cardissuers.mapper

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import javax.inject.Inject

internal class CardIssuersMapper @Inject constructor() {
    fun RemoteCardIssuerResponse.toPresentation(): List<CardIssuer> {
        val result = mutableListOf<CardIssuer>()
        result.add(
            CardIssuer(
                cardIssuerId = id.orEmpty(),
                name = name.orEmpty(),
                thumbnail = thumbnail.orEmpty()
            )
        )
        return result
    }
}

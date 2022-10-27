package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.MviResult
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer

internal sealed class CardIssuersResult : MviResult {
    sealed class GetCardIssuersResult : CardIssuersResult() {
        object InProgress : GetCardIssuersResult()
        data class Success(val cardIssuers: List<CardIssuer>) : GetCardIssuersResult()
        object Error : GetCardIssuersResult()
    }
}

package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.MviUserIntent

internal sealed class CardIssuersUIntent : MviUserIntent {
    data class InitialUIntent(val paymentMethodId: String) : CardIssuersUIntent()
    data class RetrySeeCardIssuers(val paymentMethodId: String) : CardIssuersUIntent()
}

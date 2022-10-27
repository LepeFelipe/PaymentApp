package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.MviAction

internal sealed class CardIssuersAction : MviAction {
    data class GetCardIssuersAction(val paymentMethodId: String) : CardIssuersAction()
}

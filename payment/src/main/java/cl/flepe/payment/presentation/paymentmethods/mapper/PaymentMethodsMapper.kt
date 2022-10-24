package cl.flepe.payment.presentation.paymentmethods.mapper

import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import javax.inject.Inject

internal class PaymentMethodsMapper @Inject constructor() {
    fun RemoteCreditCardResponse.toPresentation(): List<CreditCard> {
        val result = mutableListOf<CreditCard>()
        result.add(
            CreditCard(
                cardId = id.orEmpty(),
                name = name.orEmpty(),
                thumbnail = thumbnail.orEmpty()
            )
        )

        return result
    }
}

package cl.flepe.payment.factory

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemoteInstallments
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard

internal object PaymentFactory {

    fun makeRemoteCreditCardResponseList(count: Int): List<RemoteCreditCardResponse> =
        (0..count).map {
            makeRemoteCreditCardResponse()
        }

    fun makeRemoteCreditCardResponse() = RemoteCreditCardResponse(
        id = generateString(),
        name = generateString(),
        thumbnail = generateString()
    )

    fun makeNullRemoteCreditCardResponseList(count: Int): List<RemoteCreditCardResponse> =
        (0..count).map {
            makeNullRemoteCreditCardResponse()
        }

   private fun makeNullRemoteCreditCardResponse() = RemoteCreditCardResponse(
        id = null,
        name = null,
        thumbnail = null
    )

    fun makeRemoteCardIssuerResponseList(count: Int): List<RemoteCardIssuerResponse> =
        (0..count).map {
            makeRemoteCardIssuerResponse()
        }

    private fun makeRemoteCardIssuerResponse() = RemoteCardIssuerResponse(
        id = generateString(),
        name = generateString(),
        thumbnail = generateString()
    )

    fun makeNullRemoteCardIssuerResponse(count: Int): List<RemoteCardIssuerResponse> =
        (0..count).map {
            makeNullRemoteCardIssuerResponse()
        }

    private fun makeNullRemoteCardIssuerResponse() = RemoteCardIssuerResponse(
        id = null,
        name = null,
        thumbnail = null
    )

    fun makeRemotePayerCostsResponse(count: Int) = RemotePayerCostsResponse(
        installments = makeRemoteInstallmentsList(count)
    )

    private fun makeRemoteInstallmentsList(count: Int): List<RemoteInstallments> = (0..count).map {
        makeRemoteInstallments()
    }

    private fun makeRemoteInstallments() = RemoteInstallments(
        recommended_message = generateString()
    )

    fun makeCreditCardList(count: Int): List<CreditCard> = (0..count).map {
        makeCreditCard()
    }

    fun makeCreditCard() = CreditCard(
        cardId = generateString(),
        name = generateString(),
        thumbnail = generateString()
    )

    fun makeCardIssuersList(count: Int): List<CardIssuer> = (0..count).map {
        makeCardIssuer()
    }

    fun makeCardIssuer() = CardIssuer(
        cardIssuerId = generateString(),
        name = generateString(),
        thumbnail = generateString()
    )
}

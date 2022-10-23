package cl.flepe.payment.factory

import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemoteInstallments
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.payment.factory.GenerateValues.generateString

internal object PaymentFactory {

    fun makeRemoteCreditCardResponseList(count: Int): List<RemoteCreditCardResponse> = (0..count).map {
        makeRemoteCreditCardResponse()
    }

    private fun makeRemoteCreditCardResponse() = RemoteCreditCardResponse(
        id = generateString(),
        name = generateString(),
        thumbnail = generateString()
    )

    fun makeRemoteCardIssuerResponseList(count: Int): List<RemoteCardIssuerResponse> = (0..count).map {
        makeRemoteCardIssuerResponse()
    }

    private fun makeRemoteCardIssuerResponse() = RemoteCardIssuerResponse(
        name = generateString(),
        thumbnail = generateString()
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
}

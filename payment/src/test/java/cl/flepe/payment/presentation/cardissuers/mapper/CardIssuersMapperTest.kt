package cl.flepe.payment.presentation.cardissuers.mapper

import cl.flepe.payment.factory.PaymentFactory.makeNullRemoteCardIssuerResponse
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCardIssuerResponseList
import org.junit.Assert
import org.junit.Test

internal class CardIssuersMapperTest {

    private val mapper = CardIssuersMapper()

    @Test
    fun `given RemoteCardIssuerResponse, when toPresentation(), then CardIssuer`() {
        val remoteCardIssuersResponse = makeRemoteCardIssuerResponseList(3)

        val cardIssuer = remoteCardIssuersResponse.flatMap { remoteCardIssuer ->
            with(mapper) {
                remoteCardIssuer.toPresentation()
            }
        }

        remoteCardIssuersResponse.zip(cardIssuer).map {
            Assert.assertEquals(it.first.id, it.second.cardIssuerId)
            Assert.assertEquals(it.first.name, it.second.name)
            Assert.assertEquals(it.first.thumbnail, it.second.thumbnail)
        }
    }

    @Test
    fun `given null RemoteCardIssuerResponse, when toPresentation(), then empty CardIssuer`() {
        val remoteCreditCards = makeNullRemoteCardIssuerResponse(3)

        val creditCard = remoteCreditCards.flatMap { remoteCreditCard ->
            with(mapper) {
                remoteCreditCard.toPresentation()
            }
        }

        remoteCreditCards.zip(creditCard).map {
            Assert.assertTrue(it.second.cardIssuerId.isEmpty())
            Assert.assertTrue(it.second.name.isEmpty())
            Assert.assertTrue(it.second.thumbnail.isEmpty())
        }
    }

}
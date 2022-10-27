package cl.flepe.payment.presentation.paymentmethods.mapper

import cl.flepe.payment.factory.PaymentFactory.makeNullRemoteCreditCardResponseList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCreditCardResponseList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class PaymentMethodsMapperTest {

    private val mapper = PaymentMethodsMapper()

    @Test
    fun `given RemoteCreditCardResponse, when toPresentation(), then CreditCard`() {
        val remoteCreditCards = makeRemoteCreditCardResponseList(3)

        val creditCard = remoteCreditCards.flatMap { remoteCreditCard ->
            with(mapper) {
                remoteCreditCard.toPresentation()
            }
        }

        remoteCreditCards.zip(creditCard).map {
            assertEquals(it.first.id, it.second.cardId)
            assertEquals(it.first.name, it.second.name)
            assertEquals(it.first.thumbnail, it.second.thumbnail)
        }
    }

    @Test
    fun `given null RemoteCreditCardResponse, when toPresentation(), then empty CreditCard`() {
        val remoteCreditCards = makeNullRemoteCreditCardResponseList(3)

        val creditCard = remoteCreditCards.flatMap { remoteCreditCard ->
            with(mapper) {
                remoteCreditCard.toPresentation()
            }
        }

        remoteCreditCards.zip(creditCard).map {
            assertTrue(it.second.cardId.isEmpty())
            assertTrue(it.second.name.isEmpty())
            assertTrue(it.second.thumbnail.isEmpty())
        }
    }

}
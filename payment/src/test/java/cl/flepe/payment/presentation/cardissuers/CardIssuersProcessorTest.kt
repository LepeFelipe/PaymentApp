package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.factory.GenerateValues.generateString
import cl.flepe.payment.factory.PaymentFactory.makeCardIssuersList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteCardIssuerResponseList
import cl.flepe.payment.presentation.cardissuers.mapper.CardIssuersMapper
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class CardIssuersProcessorTest {

    private val repository = mockk<PaymentDataRepository>(relaxed = true)
    private val mapper = mockk<CardIssuersMapper>(relaxed = true)
    private val executionThread = ExecutionThreadFactory.makeExecutionThread()
    private val processor = CardIssuersProcessor(repository, mapper, executionThread)

    @Test
    fun `given GetCardIssuersAction remote ok, when call 'actionProcessor', then return Result-Success`() =
        runBlocking {
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            val cardIssuers = makeCardIssuersList(2)
            val paymentMethodId = generateString()
            stubCardIssuerMapper(remoteCardIssuerResponse, cardIssuers)
            stubGetRemoteCardIssuer(paymentMethodId, remoteCardIssuerResponse)

            val results =
                processor.actionProcessor(
                    CardIssuersAction.GetCardIssuersAction(paymentMethodId)
                ).toList()

            Assert.assertEquals(results[0], CardIssuersResult.GetCardIssuersResult.InProgress)
            Assert.assertEquals(
                results[1],
                CardIssuersResult.GetCardIssuersResult.Success(cardIssuers)
            )
        }

    @Test
    fun `given GetCardIssuersAction and remote nok, when call 'actionProcessor', then return Result-Error`() =
        runBlocking {
            val error = Throwable()
            val remoteCardIssuerResponse = makeRemoteCardIssuerResponseList(2)
            val cardIssuers = makeCardIssuersList(2)
            val paymentMethodId = generateString()
            stubCardIssuerMapper(remoteCardIssuerResponse, cardIssuers)
            stubGetRemoteCardIssuerError(paymentMethodId, error)

            val results =
                processor.actionProcessor(
                    CardIssuersAction.GetCardIssuersAction(paymentMethodId)
                ).toList()

            Assert.assertEquals(results[0], CardIssuersResult.GetCardIssuersResult.InProgress)
            Assert.assertEquals(results[1], CardIssuersResult.GetCardIssuersResult.Error)
        }

    private fun stubCardIssuerMapper(
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>,
        cardIssuer: List<CardIssuer>
    ) {
        every {
            remoteCardIssuerResponse.flatMap { remoteCreditCard ->
                with(mapper) {
                    remoteCreditCard.toPresentation()
                }
            }
        } returns cardIssuer
    }

    private fun stubGetRemoteCardIssuer(
        paymentMethodId: String,
        remoteCardIssuerResponse: List<RemoteCardIssuerResponse>
    ) {
        coEvery { repository.getCardIssuers(paymentMethodId) } returns flow {
            emit(
                remoteCardIssuerResponse
            )
        }
    }

    private fun stubGetRemoteCardIssuerError(paymentMethodId: String, error: Throwable) {
        coEvery { repository.getCardIssuers(paymentMethodId) } returns flow { throw error }
    }
}
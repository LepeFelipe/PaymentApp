package cl.flepe.payment.presentation.installments.mapper

import cl.flepe.payment.factory.PaymentFactory.makeNullRemoteInstallmentsList
import cl.flepe.payment.factory.PaymentFactory.makeRemoteInstallmentsList
import cl.flepe.payment.factory.PaymentFactory.makeRemotePayerCostsResponseList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class InstallmentsMapperTest {
    private val mapper = InstallmentsMapper()

    @Test
    fun `given RemotePayerCostsResponse, when toPresentation(), then PayerCost`() {
        val remotePayerCostsResponse = makeRemotePayerCostsResponseList(2)

        val payerCosts = remotePayerCostsResponse.flatMap { remotePayerCost ->
            with(mapper) {
                remotePayerCost.toPresentation()
            }
        }

        remotePayerCostsResponse.zip(payerCosts).map {
            assertEquals(
                it.first.installments?.get(0)?.recommended_message,
                it.second.installments[0].message
            )
        }
    }

    @Test
    fun `given RemoteInstallments, when toPresentation(), then Installment`() {
        val remoteInstallments = makeRemoteInstallmentsList(3)

        val installments = remoteInstallments.map { remoteInstallment ->
            with(mapper) {
                remoteInstallment.toPresentation()
            }
        }

        remoteInstallments.zip(installments).map {
            assertEquals(it.first.recommended_message, it.second.message)
        }
    }

    @Test
    fun `given null RemoteInstallments, when toPresentation(), then empty Installment`() {
        val remoteInstallments = makeNullRemoteInstallmentsList(3)

        val creditCard = remoteInstallments.map { remoteInstallment ->
            with(mapper) {
                remoteInstallment.toPresentation()
            }
        }

        remoteInstallments.zip(creditCard).map {
            assertTrue(it.second.message.isEmpty())
        }
    }

}
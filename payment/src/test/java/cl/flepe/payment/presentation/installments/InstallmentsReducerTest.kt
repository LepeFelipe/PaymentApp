package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.ReducerException
import cl.flepe.payment.factory.PaymentFactory.makePayerCostsResponseList
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult.*
import cl.flepe.payment.presentation.installments.InstallmentsUiState.*
import org.junit.Test


internal class InstallmentsReducerTest {
    private val reducer = InstallmentsReducer()

    @Test
    fun `given DefaultUiState with Result-InProgress, when reduceWith, then return LoadingUiState`() {
        val previousUiState = DefaultUiState
        val result = InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given DefaultUiState with non Result-InProgress, when reduce, then throw exception`() {
        val previousUiState = DefaultUiState
        val result = Error

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given LoadingUiState with Result-Success, when reduce, then returns DisplayInstallmentsUiState`() {
        val payerCosts = makePayerCostsResponseList(3)
        val previousUiState = LoadingUiState
        val result = Success(payerCosts)

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is DisplayInstallmentsUiState)
    }

    @Test
    fun `given LoadingUiState with Result-Error, when reduce, then returns ErrorUiState`() {
        val previousUiState = LoadingUiState
        val result = Error

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is ErrorUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given LoadingUiState with Result-InProgress, when reduce, then throw exception`() {
        val previousUiState = LoadingUiState
        val result = InProgress

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given DisplayInstallmentsUiState with Result-InProgress, when reduceWith, then return LoadingUiState`() {
        val payerCosts = makePayerCostsResponseList(2)
        val previousUiState = DisplayInstallmentsUiState(payerCosts)
        val result = InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given DisplayInstallmentsUiState with no Result-InProgress, when reduceWith, then throw exception`() {
        val payerCosts = makePayerCostsResponseList(2)
        val previousUiState = DisplayInstallmentsUiState(payerCosts)
        val result = Success(payerCosts)

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given ErrorUiState with Result-InProgress, when reduce, then returns LoadingUiState`() {
        val previousUiState = ErrorUiState
        val result = InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given ErrorUiState with Result-Success, when reduce, then throw exception`() {
        val payerCosts = makePayerCostsResponseList(2)
        val previousUiState = ErrorUiState
        val result = Success(payerCosts)

        with(reducer) { previousUiState reduceWith result }
    }
}

package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.ReducerException
import cl.flepe.payment.factory.PaymentFactory.makeCreditCardList
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsResult.GetPaymentMethodsResult.*
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState.*
import org.junit.Test


internal class PaymentMethodsReducerTest {

    private val reducer = PaymentMethodsReducer()

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
    fun `given LoadingUiState with Result-Success, when reduce, then returns DisplayPaymentMethodsUiState`() {
        val creditCards = makeCreditCardList(3)
        val previousUiState = LoadingUiState
        val result = Success(creditCards)

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is DisplayPaymentMethodsUiState)
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
    fun `given DisplayPaymentMethodsUiState with Result-InProgress, when reduceWith, then return LoadingUiState`() {
        val creditCards = makeCreditCardList(2)
        val previousUiState = DisplayPaymentMethodsUiState(creditCards)
        val result = InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given DisplayPaymentMethodsUiState with no Result-InProgress, when reduceWith, then throw exception`() {
        val creditCard = makeCreditCardList(1)
        val previousUiState = DisplayPaymentMethodsUiState(creditCard)
        val result = Success(creditCard)

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
        val creditCards = makeCreditCardList(2)
        val previousUiState = ErrorUiState
        val result = Success(creditCards)

        with(reducer) { previousUiState reduceWith result }
    }
}
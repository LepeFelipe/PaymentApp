package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.ReducerException
import cl.flepe.payment.factory.PaymentFactory.makeCardIssuersList
import org.junit.Test

internal class CardIssuersReducerTest {
    private val reducer = CardIssuersReducer()

    @Test
    fun `given DefaultUiState with Result-InProgress, when reduceWith, then return LoadingUiState`() {
        val previousUiState = CardIssuersUiState.DefaultUiState
        val result = CardIssuersResult.GetCardIssuersResult.InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is CardIssuersUiState.LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given DefaultUiState with non Result-InProgress, when reduce, then throw exception`() {
        val previousUiState = CardIssuersUiState.DefaultUiState
        val result = CardIssuersResult.GetCardIssuersResult.Error

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given LoadingUiState with Result-Success, when reduce, then returns DisplayCardIssuersUiState`() {
        val cardIssuers = makeCardIssuersList(3)
        val previousUiState = CardIssuersUiState.LoadingUiState
        val result = CardIssuersResult.GetCardIssuersResult.Success(cardIssuers)

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is CardIssuersUiState.DisplayCardIssuersUiState)
    }

    @Test
    fun `given LoadingUiState with Result-Error, when reduce, then returns ErrorUiState`() {
        val previousUiState = CardIssuersUiState.LoadingUiState
        val result = CardIssuersResult.GetCardIssuersResult.Error

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is CardIssuersUiState.ErrorUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given LoadingUiState with Result-InProgress, when reduce, then throw exception`() {
        val previousUiState = CardIssuersUiState.LoadingUiState
        val result = CardIssuersResult.GetCardIssuersResult.InProgress

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given DisplayCardIssuersUiState with Result-InProgress, when reduceWith, then return LoadingUiState`() {
        val cardIssuers = makeCardIssuersList(2)
        val previousUiState = CardIssuersUiState.DisplayCardIssuersUiState(cardIssuers)
        val result = CardIssuersResult.GetCardIssuersResult.InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is CardIssuersUiState.LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given DisplayCardIssuersUiState with no Result-InProgress, when reduceWith, then throw exception`() {
        val cardIssuers = makeCardIssuersList(2)
        val previousUiState = CardIssuersUiState.DisplayCardIssuersUiState(cardIssuers)
        val result = CardIssuersResult.GetCardIssuersResult.Success(cardIssuers)

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given ErrorUiState with Result-InProgress, when reduce, then returns LoadingUiState`() {
        val previousUiState = CardIssuersUiState.ErrorUiState
        val result = CardIssuersResult.GetCardIssuersResult.InProgress

        val newState = with(reducer) { previousUiState reduceWith result }

        assert(newState is CardIssuersUiState.LoadingUiState)
    }

    @Test(expected = ReducerException::class)
    fun `given ErrorUiState with Result-Success, when reduce, then throw exception`() {
        val cardIssuers = makeCardIssuersList(2)
        val previousUiState = CardIssuersUiState.ErrorUiState
        val result = CardIssuersResult.GetCardIssuersResult.Success(cardIssuers)

        with(reducer) { previousUiState reduceWith result }
    }
}

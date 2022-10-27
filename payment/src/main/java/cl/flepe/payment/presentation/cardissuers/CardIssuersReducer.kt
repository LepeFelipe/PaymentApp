package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.MviReducer
import cl.flepe.mvi.ReducerException
import cl.flepe.payment.presentation.cardissuers.CardIssuersResult.GetCardIssuersResult.Error
import cl.flepe.payment.presentation.cardissuers.CardIssuersResult.GetCardIssuersResult.InProgress
import cl.flepe.payment.presentation.cardissuers.CardIssuersResult.GetCardIssuersResult.Success
import cl.flepe.payment.presentation.cardissuers.CardIssuersUiState.*
import javax.inject.Inject

internal class CardIssuersReducer @Inject constructor() :
    MviReducer<CardIssuersUiState, CardIssuersResult> {

    override fun CardIssuersUiState.reduceWith(result: CardIssuersResult): CardIssuersUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is DisplayCardIssuersUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: CardIssuersResult): CardIssuersUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: CardIssuersResult): CardIssuersUiState {
        return when (result) {
            is Success -> DisplayCardIssuersUiState(result.cardIssuers)
            is Error -> ErrorUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun DisplayCardIssuersUiState.reduceWith(result: CardIssuersResult): CardIssuersUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: CardIssuersResult): CardIssuersUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private fun unsupportedReduceCase(
        uiState: CardIssuersUiState,
        result: CardIssuersResult
    ): Throwable {
        return ReducerException(uiState, result)
    }
}

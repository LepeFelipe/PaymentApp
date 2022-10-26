package cl.flepe.payment.presentation

import androidx.lifecycle.ViewModel
import cl.flepe.mvi.flow.MviPresentation
import cl.flepe.payment.presentation.cardissuers.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
internal class CardIssuersViewModel @Inject constructor(
    private val reducer: CardIssuersReducer,
    private val actionProcessorHolder: CardIssuersProcessor
) : ViewModel(), MviPresentation<CardIssuersUIntent, CardIssuersUiState> {

    private val defaultUiState: CardIssuersUiState = CardIssuersUiState.DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<CardIssuersUIntent>): Flow<CardIssuersUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                actionProcessorHolder.actionProcessor(userIntent.toAction())
            }
            .scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun CardIssuersUIntent.toAction(): CardIssuersAction {
        return when (this) {
            is CardIssuersUIntent.InitialUIntent -> CardIssuersAction.GetCardIssuersAction(
                paymentMethodId
            )
            is CardIssuersUIntent.RetrySeeCardIssuers -> CardIssuersAction.GetCardIssuersAction(
                paymentMethodId
            )
        }
    }
}

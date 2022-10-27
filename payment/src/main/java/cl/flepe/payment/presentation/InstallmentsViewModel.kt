package cl.flepe.payment.presentation

import androidx.lifecycle.ViewModel
import cl.flepe.mvi.flow.MviPresentation
import cl.flepe.payment.presentation.installments.*
import cl.flepe.payment.presentation.installments.InstallmentsAction.GetInstallmentsAction
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.InitialUIntent
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.RetrySeeInstallments
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
internal class InstallmentsViewModel @Inject constructor(
    private val reducer: InstallmentsReducer,
    private val actionProcessorHolder: InstallmentsProcessor
) : ViewModel(), MviPresentation<InstallmentsUIntent, InstallmentsUiState> {

    private val defaultUiState: InstallmentsUiState = InstallmentsUiState.DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<InstallmentsUIntent>): Flow<InstallmentsUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                actionProcessorHolder.actionProcessor(userIntent.toAction())
            }
            .scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun InstallmentsUIntent.toAction(): InstallmentsAction {
        return when (this) {
            is InitialUIntent -> GetInstallmentsAction(
                amount,
                paymentMethodId,
                issuerId
            )
            is RetrySeeInstallments -> GetInstallmentsAction(
                amount,
                paymentMethodId,
                issuerId
            )
        }
    }
}

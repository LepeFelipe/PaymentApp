package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.MviReducer
import cl.flepe.mvi.ReducerException
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult.*
import cl.flepe.payment.presentation.installments.InstallmentsUiState.*
import javax.inject.Inject

internal class InstallmentsReducer @Inject constructor() :
    MviReducer<InstallmentsUiState, InstallmentsResult> {

    override fun InstallmentsUiState.reduceWith(result: InstallmentsResult): InstallmentsUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is DisplayInstallmentsUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: InstallmentsResult): InstallmentsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: InstallmentsResult): InstallmentsUiState {
        return when (result) {
            is Success -> DisplayInstallmentsUiState(result.payerCosts)
            is Error -> ErrorUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun DisplayInstallmentsUiState.reduceWith(result: InstallmentsResult): InstallmentsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: InstallmentsResult): InstallmentsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private fun unsupportedReduceCase(
        uiState: InstallmentsUiState,
        result: InstallmentsResult
    ): Throwable {
        return ReducerException(uiState, result)
    }
}

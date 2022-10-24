package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviReducer
import cl.flepe.mvi.ReducerException
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsResult.GetPaymentMethodsResult.*
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState.*
import javax.inject.Inject

internal class PaymentMethodsReducer @Inject constructor() :
    MviReducer<PaymentMethodsUiState, PaymentMethodsResult> {

    override fun PaymentMethodsUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is DisplayPaymentMethodsUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        return when (result) {
            is Success -> DisplayPaymentMethodsUiState(result.creditCards)
            is Error -> ErrorUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun DisplayPaymentMethodsUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw unsupportedReduceCase(this, result)
        }
    }

    private fun unsupportedReduceCase(
        uiState: PaymentMethodsUiState,
        result: PaymentMethodsResult
    ): Throwable {
        return ReducerException(uiState, result)
    }
}

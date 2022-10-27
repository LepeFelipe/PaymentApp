package cl.flepe.payment.presentation

import androidx.lifecycle.ViewModel
import cl.flepe.mvi.flow.MviPresentation
import cl.flepe.payment.presentation.paymentmethods.*
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsAction.GetPaymentMethodsAction
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.InitialUIntent
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.RetrySeePaymentMethods
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
internal class PaymentMethodsViewModel @Inject constructor(
    private val reducer: PaymentMethodsReducer,
    private val actionProcessorHolder: PaymentMethodsProcessor
) : ViewModel(), MviPresentation<PaymentMethodsUIntent, PaymentMethodsUiState> {

    private val defaultUiState: PaymentMethodsUiState = PaymentMethodsUiState.DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<PaymentMethodsUIntent>): Flow<PaymentMethodsUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                actionProcessorHolder.actionProcessor(userIntent.toAction())
            }
            .scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun PaymentMethodsUIntent.toAction(): PaymentMethodsAction {
        return when (this) {
            is InitialUIntent -> GetPaymentMethodsAction
            is RetrySeePaymentMethods -> GetPaymentMethodsAction
        }
    }
}

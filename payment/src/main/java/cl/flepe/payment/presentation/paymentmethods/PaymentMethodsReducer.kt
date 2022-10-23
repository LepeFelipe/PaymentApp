package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.MviReducer
import javax.inject.Inject

internal class PaymentMethodsReducer @Inject constructor() :
    MviReducer<PaymentMethodsUiState, PaymentMethodsResult> {


    override fun PaymentMethodsUiState.reduceWith(result: PaymentMethodsResult): PaymentMethodsUiState {
        TODO("Not yet implemented")
    }
}

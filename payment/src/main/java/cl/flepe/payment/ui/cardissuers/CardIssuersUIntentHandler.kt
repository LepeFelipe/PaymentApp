package cl.flepe.payment.ui.cardissuers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.InitialUIntent
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent.RetrySeePaymentMethods
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CardIssuersUIntentHandler @Inject constructor() {
    lateinit var viewLifecycleOwner: LifecycleOwner
    private val userIntents: MutableSharedFlow<PaymentMethodsUIntent> = MutableSharedFlow()

    internal fun userIntents(): Flow<PaymentMethodsUIntent> = merge(
        initialUserIntent(),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(): Flow<PaymentMethodsUIntent> = flow {
        emit(InitialUIntent)
    }

    fun onRetrySeePaymentMethodsTapped() {
        viewLifecycleOwner.lifecycleScope.launch {
            userIntents.emit(RetrySeePaymentMethods)
        }
    }
}

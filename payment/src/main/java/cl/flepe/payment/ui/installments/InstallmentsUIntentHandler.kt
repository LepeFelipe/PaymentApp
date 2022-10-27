package cl.flepe.payment.ui.installments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cl.flepe.payment.presentation.installments.InstallmentsUIntent
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.InitialUIntent
import cl.flepe.payment.presentation.installments.InstallmentsUIntent.RetrySeeInstallments
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class InstallmentsUIntentHandler @Inject constructor() {
    lateinit var viewLifecycleOwner: LifecycleOwner
    private val userIntents: MutableSharedFlow<InstallmentsUIntent> = MutableSharedFlow()

    internal fun userIntents(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): Flow<InstallmentsUIntent> = merge(
        initialUserIntent(amount, paymentMethodId, issuerId),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): Flow<InstallmentsUIntent> = flow {
        emit(InitialUIntent(amount, paymentMethodId, issuerId))
    }

    fun onRetrySeeInstallmentsTapped(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            userIntents.emit(RetrySeeInstallments(amount, paymentMethodId, issuerId))
        }
    }
}

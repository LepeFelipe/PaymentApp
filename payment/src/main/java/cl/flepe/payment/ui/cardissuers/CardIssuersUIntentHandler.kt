package cl.flepe.payment.ui.cardissuers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cl.flepe.payment.presentation.cardissuers.CardIssuersUIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CardIssuersUIntentHandler @Inject constructor() {
    lateinit var viewLifecycleOwner: LifecycleOwner
    private val userIntents: MutableSharedFlow<CardIssuersUIntent> = MutableSharedFlow()

    internal fun userIntents(paymentMethodId: String): Flow<CardIssuersUIntent> = merge(
        initialUserIntent(paymentMethodId),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(paymentMethodId: String): Flow<CardIssuersUIntent> = flow {
        emit(CardIssuersUIntent.InitialUIntent(paymentMethodId))
    }

    fun onRetrySeeCardIssuersTapped(paymentMethodId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            userIntents.emit(CardIssuersUIntent.RetrySeeCardIssuers(paymentMethodId))
        }
    }
}

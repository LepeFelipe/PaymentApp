package cl.flepe.payment.ui.navigator

import android.view.View
import androidx.navigation.findNavController
import cl.flepe.payment.ui.amount.AmountFragmentDirections
import javax.inject.Inject

class PaymentNavigator @Inject constructor() {

    internal fun goToPaymentMethods(
        view: View,
        amount: String,
    ) {
        val direction = AmountFragmentDirections.actionEnterAmountToPaymentmethods(amount)
        view.findNavController().currentDestination?.getAction(direction.actionId)
            ?.let { view.findNavController().navigate(direction) }
    }
}
package cl.flepe.payment.ui.navigator

import android.view.View
import androidx.navigation.findNavController
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import cl.flepe.payment.ui.amount.AmountFragmentDirections
import cl.flepe.payment.ui.cardissuers.CardIssuersFragmentDirections
import cl.flepe.payment.ui.paymentmethods.PaymentMethodsFragmentDirections
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

    internal fun goToCardIssuers(
        view: View,
        amount: String,
        creditCard: CreditCard
    ) {
        val direction =
            PaymentMethodsFragmentDirections.actionPaymentmethodsToCardissuers(amount, creditCard)
        view.findNavController().currentDestination?.getAction(direction.actionId)
            ?.let { view.findNavController().navigate(direction) }
    }

    internal fun goToInstallments(
        view: View,
        amount: String,
        creditCard: CreditCard,
        cardIssuer: CardIssuer
    ) {
        val direction =
            CardIssuersFragmentDirections.actionCardissuersToInstallments(
                amount,
                creditCard,
                cardIssuer
            )
        view.findNavController().currentDestination?.getAction(direction.actionId)
            ?.let { view.findNavController().navigate(direction) }
    }
}

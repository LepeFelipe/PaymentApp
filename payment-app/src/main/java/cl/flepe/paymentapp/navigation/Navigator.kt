package cl.flepe.paymentapp.navigation

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class Navigator @Inject constructor() {
    fun showPaymentFeature(activity: FragmentActivity?) {
        activity?.startActivity(
            NavIntent.getIntentByPath(
                activity,
                NavDestination.PaymentsActivityPath()
            )
        )
    }
}
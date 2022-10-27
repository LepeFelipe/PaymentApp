package cl.flepe.paymentapp.navigation

sealed class NavDestination(val path: String) {

    class PaymentsActivityPath(activityPath: String) : NavDestination(path = activityPath) {
        companion object {
            operator fun invoke(): PaymentsActivityPath =
                PaymentsActivityPath(PAYMENT_ACTIVITY_PATH)
        }
    }

    private companion object {
        const val PAYMENT_ACTIVITY_PATH = "cl.flepe.payment.ui.PaymentActivity"
    }
}
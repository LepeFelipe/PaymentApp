package cl.flepe.payment.ui.provider

import android.content.Context
import androidx.annotation.StringRes
import cl.flepe.payment.R.string.payment_error_occurred
import cl.flepe.payment.R.string.payment_retry
import cl.flepe.payment_components.components.AttrsGenericErrorComponent
import javax.inject.Inject

class PaymentProviderImpl @Inject constructor(private val context: Context) : PaymentProvider {
    override fun forGenericError(action: () -> Unit): AttrsGenericErrorComponent {
        return AttrsGenericErrorComponent(
            description = getString(payment_error_occurred),
            btnName = getString(payment_retry),
            clickBtnListener = action
        )
    }

    private fun getString(@StringRes id: Int) = context.getString(id)
}

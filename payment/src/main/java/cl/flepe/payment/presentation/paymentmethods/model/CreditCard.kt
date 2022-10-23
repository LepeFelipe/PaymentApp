package cl.flepe.payment.presentation.paymentmethods.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditCard(
    val cardId: String,
    val name: String,
    val thumbnail: String
) : Parcelable

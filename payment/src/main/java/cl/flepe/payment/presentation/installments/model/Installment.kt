package cl.flepe.payment.presentation.installments.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Installment(
    val message: String
) : Parcelable

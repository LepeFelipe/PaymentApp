package cl.flepe.payment.presentation.cardissuers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardIssuer(
    val cardIssuerId: String,
    val name: String,
    val thumbnail: String
) : Parcelable

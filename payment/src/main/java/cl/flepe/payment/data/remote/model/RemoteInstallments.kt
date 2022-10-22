package cl.flepe.payment.data.remote.model

import cl.flepe.payment.data.remote.model.Constants.RECOMMENDED_MESSAGE
import com.google.gson.annotations.SerializedName

internal data class RemoteInstallments(
    @SerializedName(RECOMMENDED_MESSAGE) val recommended_message: String?
)
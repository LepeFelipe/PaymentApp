package cl.flepe.payment.data.remote.model

import cl.flepe.payment.data.remote.model.Constants.NAME
import cl.flepe.payment.data.remote.model.Constants.THUMBNAIL
import com.google.gson.annotations.SerializedName

internal data class RemoteCreditCardResponse(
    @SerializedName(NAME) val name: String?,
    @SerializedName(THUMBNAIL) val thumbnail: String?
)
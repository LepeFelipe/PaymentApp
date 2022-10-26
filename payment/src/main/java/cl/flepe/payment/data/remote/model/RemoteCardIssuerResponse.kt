package cl.flepe.payment.data.remote.model

import cl.flepe.payment.data.remote.model.Constants.ID
import cl.flepe.payment.data.remote.model.Constants.NAME
import cl.flepe.payment.data.remote.model.Constants.THUMBNAIL
import com.google.gson.annotations.SerializedName

internal data class RemoteCardIssuerResponse(
    @SerializedName(ID) val id: String?,
    @SerializedName(NAME) val name: String?,
    @SerializedName(THUMBNAIL) val thumbnail: String?
)
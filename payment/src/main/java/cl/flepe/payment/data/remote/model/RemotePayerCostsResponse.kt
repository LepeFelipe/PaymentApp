package cl.flepe.payment.data.remote.model

import cl.flepe.payment.data.remote.model.Constants.PAYER_COSTS
import com.google.gson.annotations.SerializedName

internal data class RemotePayerCostsResponse(
    @SerializedName(PAYER_COSTS) val installments: List<RemoteInstallments>?
)
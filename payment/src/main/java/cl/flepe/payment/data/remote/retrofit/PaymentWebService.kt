package cl.flepe.payment.data.remote.retrofit

import cl.flepe.payment.data.remote.model.Constants.AMOUNT
import cl.flepe.payment.data.remote.model.Constants.ISSUER_ID
import cl.flepe.payment.data.remote.model.Constants.PAYMENT_METHOD_ID
import cl.flepe.payment.data.remote.model.Constants.PUBLIC_KEY
import cl.flepe.payment.data.remote.model.RemoteCardIssuerResponse
import cl.flepe.payment.data.remote.model.RemoteCreditCardResponse
import cl.flepe.payment.data.remote.model.RemotePayerCostsResponse
import cl.flepe.paymentapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PaymentWebService {

    @GET("payment_methods")
    suspend fun getPaymentMethods(@Query(PUBLIC_KEY) publicKey: String = BuildConfig.PAYMENT_KEY): List<RemoteCreditCardResponse>

    @GET("payment_methods/card_issuers")
    suspend fun getCardIssuers(
        @Query(PUBLIC_KEY) publicKey: String = BuildConfig.PAYMENT_KEY,
        @Query(PAYMENT_METHOD_ID) paymentMethodId: String
    ): List<RemoteCardIssuerResponse>

    @GET("payment_methods/installments")
    suspend fun getInstallments(
        @Query(PUBLIC_KEY) publicKey: String = BuildConfig.PAYMENT_KEY,
        @Query(AMOUNT) amount: Int,
        @Query(PAYMENT_METHOD_ID) paymentMethodId: String,
        @Query(ISSUER_ID) issuerId: String
    ): RemotePayerCostsResponse
}
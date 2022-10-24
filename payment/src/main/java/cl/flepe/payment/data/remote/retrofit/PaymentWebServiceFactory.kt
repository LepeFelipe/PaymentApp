package cl.flepe.payment.data.remote.retrofit

import cl.flepe.payment.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class PaymentWebServiceFactory {

    fun createRetrofit(): PaymentWebService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL_MERCADO_PAGO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PaymentWebService::class.java)
    }
}

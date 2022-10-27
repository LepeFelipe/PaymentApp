package cl.flepe.payment.data.remote.retrofit

import android.content.Context
import cl.flepe.payment.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory

internal class PaymentWebServiceFactory {

    fun createRetrofit(context: Context): PaymentWebService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL_MERCADO_PAGO)
            .client(makeOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PaymentWebService::class.java)
    }

    private fun makeOkHttpClient(context: Context): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        if (BuildConfig.FLAVOR == FLAVOR_DEV) {
            okHttpBuilder.addInterceptor(FakeInterceptor(context))
        } else {
            okHttpBuilder.addInterceptor(makeDebugLoggingInterceptor())
        }
        okHttpBuilder.socketFactory(SocketFactory.getDefault())
        okHttpBuilder.connectTimeout(Timeout.CONNECT, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(Timeout.READ, TimeUnit.SECONDS)
        return okHttpBuilder.build()
    }

    private fun makeDebugLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    companion object {
        const val FLAVOR_DEV = "dev"

        object Timeout {
            const val CONNECT: Long = 60
            const val READ: Long = 60
        }
    }
}


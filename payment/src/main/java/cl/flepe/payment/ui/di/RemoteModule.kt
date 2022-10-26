package cl.flepe.payment.ui.di

import android.content.Context
import cl.flepe.payment.data.remote.PaymentRemoteImpl
import cl.flepe.payment.data.remote.retrofit.PaymentWebService
import cl.flepe.payment.data.remote.retrofit.PaymentWebServiceFactory
import cl.flepe.payment.data.source.PaymentRemote
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RemoteModule {

    @Binds
    internal abstract fun bindPaymentRemote(paymentRemoteImpl: PaymentRemoteImpl): PaymentRemote

    companion object {
        @Provides
        internal fun provideWebServiceRetrofit(context: Context): PaymentWebService = PaymentWebServiceFactory()
            .createRetrofit(context)
    }
}

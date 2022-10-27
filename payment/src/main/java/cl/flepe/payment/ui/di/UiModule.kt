package cl.flepe.payment.ui.di

import cl.flepe.payment.ui.provider.PaymentProvider
import cl.flepe.payment.ui.provider.PaymentProviderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UiModule {
    @Binds
    internal abstract fun bindPaymentComponentProvider(
        paymentProvider: PaymentProviderImpl
    ): PaymentProvider
}
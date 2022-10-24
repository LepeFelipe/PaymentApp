package cl.flepe.payment.ui.di

import cl.flepe.payment.ui.PaymentActivity
import cl.flepe.payment.ui.amount.AmountFragment
import cl.flepe.paymentapp.di.ActivityScope
import cl.flepe.paymentapp.di.AppComponent
import dagger.Component

@ActivityScope
@Component(
    modules = [PresentationModule::class, RemoteModule::class],
    dependencies = [AppComponent::class]
)
abstract class PaymentComponent {

    internal abstract fun inject(amountFragment: AmountFragment)
    internal abstract fun inject(activity: PaymentActivity)
}

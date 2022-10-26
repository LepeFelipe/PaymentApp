package cl.flepe.payment.ui.di

import cl.flepe.payment.ui.PaymentActivity
import cl.flepe.payment.ui.amount.AmountFragment
import cl.flepe.payment.ui.cardissuers.CardIssuersFragment
import cl.flepe.payment.ui.installments.InstallmentsFragment
import cl.flepe.payment.ui.paymentmethods.PaymentMethodsFragment
import cl.flepe.payment.ui.resume.ResumeFragment
import cl.flepe.paymentapp.di.ActivityScope
import cl.flepe.paymentapp.di.AppComponent
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ActivityScope
@Component(
    modules = [PresentationModule::class, RemoteModule::class],
    dependencies = [AppComponent::class]
)
abstract class PaymentComponent {

    internal abstract fun inject(amountFragment: AmountFragment)
    @FlowPreview
    @ExperimentalCoroutinesApi
    internal abstract fun inject(paymentMethodsFragment: PaymentMethodsFragment)
    @FlowPreview
    @ExperimentalCoroutinesApi
    internal abstract fun inject(cardIssuersFragment: CardIssuersFragment)
    @FlowPreview
    @ExperimentalCoroutinesApi
    internal abstract fun inject(installmentsFragment: InstallmentsFragment)
    internal abstract fun inject(resumeFragment: ResumeFragment)
    internal abstract fun inject(activity: PaymentActivity)
}

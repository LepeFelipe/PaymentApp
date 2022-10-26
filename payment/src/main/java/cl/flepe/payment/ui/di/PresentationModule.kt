package cl.flepe.payment.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.flepe.mvi.flow.ExecutionThread
import cl.flepe.mvi.flow.ExecutionThreadFactory
import cl.flepe.payment.presentation.CardIssuersViewModel
import cl.flepe.payment.presentation.PaymentMethodsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module
abstract class PresentationModule {

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(PaymentMethodsViewModel::class)
    internal abstract fun bindPaymentMethodsViewModel(paymentMethodsViewModel: PaymentMethodsViewModel): ViewModel

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CardIssuersViewModel::class)
    internal abstract fun bindCardIssuersViewModel(cardIssuersViewModel: CardIssuersViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        fun provideExecutionThread(): ExecutionThread = ExecutionThreadFactory.makeExecutionThread()
    }
}

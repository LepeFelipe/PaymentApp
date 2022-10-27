package cl.flepe.paymentapp.di

import cl.flepe.paymentapp.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class]
)
interface MainComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): MainComponent
    }

    fun inject(activity: MainActivity)
}

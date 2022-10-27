package cl.flepe.paymentapp

import android.app.Application
import cl.flepe.paymentapp.di.AppComponent
import cl.flepe.paymentapp.di.DaggerAppComponent

class PaymentApp: Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent = DaggerAppComponent
        .factory()
        .create(
            application = this,
            context = applicationContext
        )
}
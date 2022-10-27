package cl.flepe.paymentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.flepe.paymentapp.databinding.ActivityMainBinding
import cl.flepe.paymentapp.di.DaggerMainComponent
import cl.flepe.paymentapp.navigation.Navigator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupInjection()
        setupButtonListener()
    }

    private fun setupInjection() {
        val component = (applicationContext as PaymentApp).appComponent
        DaggerMainComponent
            .factory()
            .create(component)
            .inject(this)
    }

    private fun setupButtonListener() {
        binding?.btnNavToPayment?.setOnClickListener {
            navigator.showPaymentFeature(this)
        }
    }
}

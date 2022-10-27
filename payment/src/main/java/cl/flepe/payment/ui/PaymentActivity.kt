package cl.flepe.payment.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import cl.flepe.payment.R
import cl.flepe.payment.databinding.ActivityPaymentBinding
import cl.flepe.payment.ui.di.DaggerPaymentComponent
import cl.flepe.paymentapp.PaymentApp

class PaymentActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    var binding: ActivityPaymentBinding? = null
    var listener =
        NavController.OnDestinationChangedListener { _, _, _ ->
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityPaymentBinding.inflate(
                layoutInflater
            )
        setContentView(binding?.root)
        navController = Navigation.findNavController(this, R.id.payment_nav_fragment)
        setupActivity()
    }

    private fun setupActivity() {
        setupInjection()
        setupNavigation()
    }

    private fun setupInjection() {
        val component = (applicationContext as PaymentApp).appComponent
        DaggerPaymentComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    private fun setupNavigation() {
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding?.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        fun launchIntent(context: Context?): Intent? {
            return context?.let {
                Intent(context, PaymentActivity::class.java)
            }
        }
    }
}

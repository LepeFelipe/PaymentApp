package cl.flepe.payment.ui.amount


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cl.flepe.payment.databinding.FragmentAmountBinding
import cl.flepe.payment.ui.di.DaggerPaymentComponent
import cl.flepe.paymentapp.PaymentApp

internal class AmountFragment : Fragment() {

    private var binding: FragmentAmountBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            FragmentAmountBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    private fun setupInjection() {
        val component = (activity?.applicationContext as PaymentApp).appComponent
        DaggerPaymentComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
package cl.flepe.payment.ui.amount


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cl.flepe.payment.R.string.payment_enter_amount_before_continue
import cl.flepe.payment.databinding.FragmentAmountBinding
import cl.flepe.payment.ui.di.DaggerPaymentComponent
import cl.flepe.payment.ui.navigator.PaymentNavigator
import cl.flepe.paymentapp.PaymentApp
import javax.inject.Inject

internal class AmountFragment : Fragment() {

    @Inject
    lateinit var paymentNavigator: PaymentNavigator

    var binding: FragmentAmountBinding? = null

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
            binding = FragmentAmountBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAmountListener()
    }

    private fun setupInjection() {
        val component = (activity?.applicationContext as PaymentApp).appComponent
        DaggerPaymentComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    private fun btnAmountListener() {
        binding?.btnAmountContinue?.setOnClickListener {
            navToSelectPaymentMethods()
        }
    }

    private fun navToSelectPaymentMethods() {
        binding?.apply {
            if (editTextAmount.text?.isNotEmpty() == true) {
                binding?.let { safeBinding ->
                    paymentNavigator.goToPaymentMethods(
                        safeBinding.root,
                        editTextAmount.text.toString()
                    )
                }
            } else {
                context?.let { safeContext ->
                    Toast.makeText(
                        safeContext,
                        getString(payment_enter_amount_before_continue),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

package cl.flepe.payment.ui.resume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import cl.flepe.payment.R
import cl.flepe.payment.databinding.FragmentResumeBinding
import cl.flepe.payment.ui.di.DaggerPaymentComponent
import cl.flepe.paymentapp.PaymentApp
import com.bumptech.glide.Glide

class ResumeFragment : Fragment() {

    var binding: FragmentResumeBinding? = null

    private val args: ResumeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentResumeBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResume()
    }

    private fun setupInjection() {
        val component = (activity?.applicationContext as PaymentApp).appComponent
        DaggerPaymentComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    private fun setupResume() {
        binding?.apply {
            context?.let { safeContext ->
                textviewAmount.text = "${getString(R.string.payment_total)}: $${args.amountParam}"
                if (args.creditCard.thumbnail.isNotEmpty()) {
                    Glide.with(safeContext).load(args.creditCard.thumbnail).into(imgCreditCardIcon)
                }
                textviewCreditCard.text =
                    "${getString(R.string.payment_credit_card)}: ${args.creditCard.name}"

                if (args.cardIssuer.thumbnail.isNotEmpty()) {
                    Glide.with(safeContext).load(args.cardIssuer.thumbnail).into(imgCardIssuerIcon)
                }
                textviewCardIssuer.text =
                    "${getString(R.string.payment_bank)}: ${args.cardIssuer.name}"

                textviewInstallment.text =
                    "${getString(R.string.payment_installment_selected)}: ${args.installment.message}"
            }

            btnResumeBack.setOnClickListener {
                activity?.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
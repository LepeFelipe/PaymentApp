package cl.flepe.payment.ui.paymentmethods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import cl.flepe.mvi.flow.MviUi
import cl.flepe.payment.databinding.FragmentPaymentMethodsBinding
import cl.flepe.payment.presentation.PaymentMethodsViewModel
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUIntent
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState
import cl.flepe.payment.ui.di.DaggerPaymentComponent
import cl.flepe.paymentapp.PaymentApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
internal class PaymentMethodsFragment : Fragment(), AdapterView.OnItemSelectedListener,
    MviUi<PaymentMethodsUIntent, PaymentMethodsUiState> {

    @Inject
    lateinit var uiRender: PaymentMethodsUiRender

    @Inject
    lateinit var uIntentHandler: PaymentMethodsUIntentHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PaymentMethodsViewModel by viewModels { viewModelFactory }

    private val args: PaymentMethodsFragmentArgs by navArgs()

    var binding: FragmentPaymentMethodsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentPaymentMethodsBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiRender()
        uIntentHandler.viewLifecycleOwner = viewLifecycleOwner
        viewModel.processUserIntentsAndObserveUiStates(userIntents())
            .onEach { renderUiStates(it) }
            .launchIn(lifecycleScope)
    }

    private fun setupInjection() {
        val component = (activity?.applicationContext as PaymentApp).appComponent
        DaggerPaymentComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    private fun setupUiRender() {
        uiRender.binding = binding
        uiRender.apply {

            onRetrySeePaymentMethodsEvent = {
                uIntentHandler.onRetrySeePaymentMethodsTapped()
            }

            onGoToCardIssuersEvent = { creditCard ->
                binding?.let { safeBinding ->
                    //  navigator.goToCardIssuers(safeBinding.root, args.amountParam, creditCard)
                }
            }
        }
    }

    override fun renderUiStates(uiState: PaymentMethodsUiState) {
        uiRender.renderUiStates(uiState)
    }

    override fun userIntents(): Flow<PaymentMethodsUIntent> {
        return uIntentHandler.userIntents()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

package cl.flepe.payment.ui.installments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import cl.flepe.mvi.flow.MviUi
import cl.flepe.payment.databinding.FragmentInstallmentsBinding
import cl.flepe.payment.presentation.InstallmentsViewModel
import cl.flepe.payment.presentation.installments.InstallmentsUIntent
import cl.flepe.payment.presentation.installments.InstallmentsUiState
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
internal class InstallmentsFragment : Fragment(),
    MviUi<InstallmentsUIntent, InstallmentsUiState> {

    @Inject
    lateinit var uiRender: InstallmentsUiRender

    @Inject
    lateinit var uIntentHandler: InstallmentsUIntentHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InstallmentsViewModel by viewModels { viewModelFactory }

    private val args: InstallmentsFragmentArgs by navArgs()

    var binding: FragmentInstallmentsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentInstallmentsBinding.inflate(inflater, container, false)
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

            onRetrySeeInstallmentsEvent = {
                uIntentHandler.onRetrySeeInstallmentsTapped(
                    args.amountParam.toInt(),
                    args.creditCard.cardId,
                    args.cardIssuer.cardIssuerId
                )
            }

            onGoToResumeEvent = { installment ->
                binding?.let { safeBinding ->
                    navigator.goToResume(
                        safeBinding.root,
                        args.amountParam,
                        args.creditCard,
                        args.cardIssuer,
                        installment
                    )
                }
            }
        }
    }

    override fun renderUiStates(uiState: InstallmentsUiState) {
        uiRender.renderUiStates(uiState)
    }

    override fun userIntents(): Flow<InstallmentsUIntent> {
        return uIntentHandler.userIntents(
            args.amountParam.toInt(),
            args.creditCard.cardId,
            args.cardIssuer.cardIssuerId
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

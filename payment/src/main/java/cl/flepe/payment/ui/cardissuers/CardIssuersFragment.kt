package cl.flepe.payment.ui.cardissuers

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
import cl.flepe.payment.databinding.FragmentCardIssuersBinding
import cl.flepe.payment.presentation.CardIssuersViewModel
import cl.flepe.payment.presentation.cardissuers.CardIssuersUIntent
import cl.flepe.payment.presentation.cardissuers.CardIssuersUiState
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
internal class CardIssuersFragment : Fragment(),
    MviUi<CardIssuersUIntent, CardIssuersUiState> {

    @Inject
    lateinit var uiRender: CardIssuersUiRender

    @Inject
    lateinit var uIntentHandler: CardIssuersUIntentHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CardIssuersViewModel by viewModels { viewModelFactory }

    private val args: CardIssuersFragmentArgs by navArgs()

    var binding: FragmentCardIssuersBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentCardIssuersBinding.inflate(inflater, container, false)
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

            onRetrySeeCardIssuersEvent = {
                uIntentHandler.onRetrySeeCardIssuersTapped(args.creditCard.cardId)
            }

            onGoToInstallmentsEvent = { cardIssuer ->
                binding?.let { safeBinding ->
                    //  navigator.goToCardIssuers(safeBinding.root, args.amountParam, args.creditCard, cardIssuer)
                }
            }
        }
    }

    override fun renderUiStates(uiState: CardIssuersUiState) {
        uiRender.renderUiStates(uiState)
    }

    override fun userIntents(): Flow<CardIssuersUIntent> {
        return uIntentHandler.userIntents(args.creditCard.cardId)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

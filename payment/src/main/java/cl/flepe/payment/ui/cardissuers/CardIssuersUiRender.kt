package cl.flepe.payment.ui.cardissuers

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cl.flepe.payment.R
import cl.flepe.payment.databinding.FragmentCardIssuersBinding
import cl.flepe.payment.presentation.cardissuers.CardIssuersUiState
import cl.flepe.payment.presentation.cardissuers.CardIssuersUiState.*
import cl.flepe.payment.presentation.cardissuers.model.CardIssuer
import cl.flepe.payment.ui.cardissuers.mapper.CardIssuerMapper
import cl.flepe.payment.ui.customSpinner.adapter.CustomSpinnerAdapter
import cl.flepe.payment.ui.navigator.PaymentNavigator
import javax.inject.Inject

class CardIssuersUiRender @Inject constructor(private val context: Context) {

    lateinit var adapter: CustomSpinnerAdapter
    var binding: FragmentCardIssuersBinding? = null
    var onRetrySeeCardIssuersEvent: () -> Unit = { }
    internal var onGoToInstallmentsEvent: (CardIssuer) -> Unit = { }

    @Inject
    lateinit var navigator: PaymentNavigator

    @Inject
    lateinit var mapper: CardIssuerMapper

    internal fun renderUiStates(uiState: CardIssuersUiState) {
        when (uiState) {
            DefaultUiState -> {}

            LoadingUiState -> {
                showLoading()
            }

            ErrorUiState -> {
                showGenericError()
                hideLoading()
            }

            is DisplayCardIssuersUiState -> {
                setupDisplayCardIssuers(uiState.cardIssuers)
                hideLoading()
            }
        }
    }

    private fun setupDisplayCardIssuers(cardIssuers: List<CardIssuer>) {
        binding?.apply {
            adapter = CustomSpinnerAdapter(context, cardIssuers.flatMap { cardIssuer ->
                with(mapper) {
                    cardIssuer.toAttrsCustomSpinner()
                }
            })
            spinnerCardIssuers.adapter = adapter

            spinnerCardIssuers.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        btnCardissuersContinue.setOnClickListener {
                            onGoToInstallmentsEvent(cardIssuers[position])
                        }
                    }

                }
            showCardIssuers()
        }
    }

    private fun showCardIssuers() {
        binding?.apply {
            genericError.isGone = true
            spinnerCardIssuers.isVisible = true
            btnCardissuersContinue.isVisible = true
        }
    }

    private fun showLoading() {
        binding?.apply {
            genericError.isGone = true
            spinnerCardIssuers.isGone = true
            btnCardissuersContinue.isGone = true
            loading.isVisible = true
        }
    }

    private fun hideLoading() {
        binding?.loading?.isGone = true
    }

    private fun showGenericError() {
        binding?.apply {
            textviewError.text = context.getString(R.string.payment_error_occurred)
            btnError.setOnClickListener {
                onRetrySeeCardIssuersEvent()
            }
            genericError.isVisible = true
        }
    }
}

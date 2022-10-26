package cl.flepe.payment.ui.paymentmethods

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cl.flepe.payment.databinding.FragmentPaymentMethodsBinding
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState.*
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import cl.flepe.payment.ui.navigator.PaymentNavigator
import cl.flepe.payment.ui.paymentmethods.mapper.UiCreditCardMapper
import cl.flepe.payment.ui.provider.PaymentProvider
import cl.flepe.payment_components.components.CustomSpinnerAdapter
import javax.inject.Inject

class PaymentMethodsUiRender @Inject constructor(private val context: Context) {

    lateinit var adapter: CustomSpinnerAdapter
    var binding: FragmentPaymentMethodsBinding? = null
    var onRetrySeePaymentMethodsEvent: () -> Unit = { }
    internal var onGoToCardIssuersEvent: (CreditCard) -> Unit = { }

    @Inject
    lateinit var navigator: PaymentNavigator

    @Inject
    lateinit var mapper: UiCreditCardMapper

    @Inject
    lateinit var uiProvider: PaymentProvider

    internal fun renderUiStates(uiState: PaymentMethodsUiState) {
        when (uiState) {
            DefaultUiState -> {}

            LoadingUiState -> {
                showLoading()
            }

            ErrorUiState -> {
                showGenericError()
                hideLoading()
            }

            is DisplayPaymentMethodsUiState -> {
                setupDisplayCreditCards(uiState.creditCard)
                hideLoading()
            }
        }
    }

    private fun setupDisplayCreditCards(creditCards: List<CreditCard>) {
        binding?.apply {
            adapter = CustomSpinnerAdapter(context, creditCards.flatMap { creditCard ->
                with(mapper) {
                    creditCard.toAttrsCustomSpinner()
                }
            })
            spinnerCreditCards.adapter = adapter

            spinnerCreditCards.onItemSelectedListener =
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
                        btnPaymentmethodsContinue.setOnClickListener {
                            onGoToCardIssuersEvent(creditCards[position])
                        }
                    }

                }
            showCreditCards()
        }
    }

    private fun showCreditCards() {
        binding?.apply {
            genericError.isGone = true
            spinnerCreditCards.isVisible = true
            btnPaymentmethodsContinue.isVisible = true
        }
    }

    private fun showLoading() {
        binding?.apply {
            genericError.isGone = true
            spinnerCreditCards.isGone = true
            btnPaymentmethodsContinue.isGone = true
            loading.isVisible = true
        }
    }

    private fun hideLoading() {
        binding?.loading?.isGone = true
    }

    private fun showGenericError() {
        binding?.apply {
            uiProvider.forGenericError {
                onRetrySeePaymentMethodsEvent()
            }
            genericError.isVisible = true
        }
    }
}
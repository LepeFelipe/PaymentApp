package cl.flepe.payment.ui.paymentmethods

import android.content.Context
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cl.flepe.payment.databinding.FragmentPaymentMethodsBinding
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsUiState.*
import cl.flepe.payment.presentation.paymentmethods.model.CreditCard
import cl.flepe.payment.ui.customSpinner.adapter.CustomSpinnerAdapter
import cl.flepe.payment.ui.navigator.PaymentNavigator
import cl.flepe.payment.ui.paymentmethods.mapper.CreditCardMapper
import javax.inject.Inject

class PaymentMethodsUiRender @Inject constructor(private val context: Context) {

    lateinit var adapter: CustomSpinnerAdapter
    var binding: FragmentPaymentMethodsBinding? = null
    var onRetrySeePaymentMethodsEvent: () -> Unit = { }
    internal var onGoToCardIssuersEvent: (CreditCard) -> Unit = { }

    @Inject
    lateinit var navigator: PaymentNavigator

    @Inject
    lateinit var mapper: CreditCardMapper

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


            /*  adapter.cli = { item ->
                  onGoToCardIssuersEvent(
                      CreditCard(
                          cardId = item.id,
                          name = item.leftLabel.toString(),
                          thumbnail = ""
                      )
                  )
              } */
            showCreditCards()
        }
    }

    private fun showCreditCards() {
        binding?.apply {
            genericError.isGone = true
            spinnerCreditCards.isVisible = true
        }
    }

    private fun showLoading() {
        binding?.apply {
            genericError.isGone = true
            spinnerCreditCards.isGone = true
            loading.isVisible = true
        }
    }

    private fun hideLoading() {
        binding?.loading?.isGone = true
    }

    private fun showGenericError() {
        binding?.apply {
            textviewError.text = "Ha ocurrido un error, intentelo nuevamente"
            btnError.setOnClickListener {
                onRetrySeePaymentMethodsEvent()
            }
            genericError.isVisible = true
        }
    }
}
package cl.flepe.payment.ui.installments

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cl.flepe.payment.databinding.FragmentInstallmentsBinding
import cl.flepe.payment.presentation.installments.InstallmentsUiState
import cl.flepe.payment.presentation.installments.InstallmentsUiState.*
import cl.flepe.payment.presentation.installments.model.Installment
import cl.flepe.payment.presentation.installments.model.PayerCost
import cl.flepe.payment.ui.installments.mapper.UiInstallmentsMapper
import cl.flepe.payment.ui.navigator.PaymentNavigator
import cl.flepe.payment.ui.provider.PaymentProvider
import cl.flepe.payment_components.components.CustomSpinnerAdapter
import javax.inject.Inject

class InstallmentsUiRender @Inject constructor(private val context: Context) {

    lateinit var adapter: CustomSpinnerAdapter
    var binding: FragmentInstallmentsBinding? = null
    var onRetrySeeInstallmentsEvent: () -> Unit = { }
    internal var onGoToResumeEvent: (Installment) -> Unit = { }

    @Inject
    lateinit var navigator: PaymentNavigator

    @Inject
    lateinit var mapper: UiInstallmentsMapper

    @Inject
    lateinit var uiProvider: PaymentProvider

    internal fun renderUiStates(uiState: InstallmentsUiState) {
        when (uiState) {
            DefaultUiState -> {}

            LoadingUiState -> {
                showLoading()
            }

            ErrorUiState -> {
                showGenericError()
                hideLoading()
            }

            is DisplayInstallmentsUiState -> {
                setupDisplayInstallments(uiState.payerCosts)
                hideLoading()
            }
        }
    }

    private fun setupDisplayInstallments(payerCosts: List<PayerCost>) {
        binding?.apply {
            adapter = CustomSpinnerAdapter(context, payerCosts.flatMap { payerCost ->
                with(mapper) {
                    payerCost.installments.flatMap { it.toAttrsCustomSpinner() }
                }
            })
            spinnerInstallments.adapter = adapter

            spinnerInstallments.onItemSelectedListener =
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
                        btnInstallmentsContinue.setOnClickListener {
                            payerCosts.map { payerCost ->
                                onGoToResumeEvent(payerCost.installments[position])
                            }
                        }
                    }

                }
            showCardIssuers()
        }
    }

    private fun showCardIssuers() {
        binding?.apply {
            genericError.isGone = true
            spinnerInstallments.isVisible = true
            btnInstallmentsContinue.isVisible = true
        }
    }

    private fun showLoading() {
        binding?.apply {
            genericError.isGone = true
            spinnerInstallments.isGone = true
            btnInstallmentsContinue.isGone = true
            loading.isVisible = true
        }
    }

    private fun hideLoading() {
        binding?.loading?.isGone = true
    }

    private fun showGenericError() {
        binding?.apply {
            uiProvider.forGenericError {
                onRetrySeeInstallmentsEvent()
            }
            genericError.isVisible = true
        }
    }
}

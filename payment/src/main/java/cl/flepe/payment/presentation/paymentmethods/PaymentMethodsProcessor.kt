package cl.flepe.payment.presentation.paymentmethods

import cl.flepe.mvi.flow.ExecutionThread
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsAction.GetPaymentMethodsAction
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsResult.GetPaymentMethodsResult
import cl.flepe.payment.presentation.paymentmethods.PaymentMethodsResult.GetPaymentMethodsResult.*
import cl.flepe.payment.presentation.paymentmethods.mapper.PaymentMethodsMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class PaymentMethodsProcessor @Inject constructor(
    private val repository: PaymentDataRepository,
    private val mapper: PaymentMethodsMapper,
    private val coroutineThreadProvider: ExecutionThread
) {
    fun actionProcessor(actions: PaymentMethodsAction): Flow<PaymentMethodsResult> =
        when (actions) {
            is GetPaymentMethodsAction -> paymentMethods()
        }

    private fun paymentMethods(): Flow<GetPaymentMethodsResult> =
        repository.getPaymentMethods()
            .map { remote ->
                Success(with(mapper) { remote.map { it.toPresentation() } }) as GetPaymentMethodsResult
            }.onStart {
                emit(InProgress)
            }.catch {
                emit(Error)
            }
            .flowOn(coroutineThreadProvider.ioThread())
}

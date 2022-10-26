package cl.flepe.payment.presentation.installments

import cl.flepe.mvi.flow.ExecutionThread
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.presentation.installments.InstallmentsAction.GetInstallmentsAction
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult.Error
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult.InProgress
import cl.flepe.payment.presentation.installments.InstallmentsResult.GetInstallmentsResult.Success
import cl.flepe.payment.presentation.installments.mapper.InstallmentsMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class InstallmentsProcessor @Inject constructor(
    private val repository: PaymentDataRepository,
    private val mapper: InstallmentsMapper,
    private val coroutineThreadProvider: ExecutionThread
) {
    fun actionProcessor(actions: InstallmentsAction): Flow<InstallmentsResult> =
        when (actions) {
            is GetInstallmentsAction -> installments(
                actions.amount,
                actions.paymentMethodId,
                actions.issuerId
            )
        }

    private fun installments(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): Flow<GetInstallmentsResult> =
        repository.getInstallments(amount, paymentMethodId, issuerId)
            .map { remoteInstallments ->
                Success(remoteInstallments.flatMap { remoteInstallment ->
                    with(mapper) {
                        remoteInstallment.toPresentation()
                    }
                }) as GetInstallmentsResult
            }.onStart {
                emit(InProgress)
            }.catch {
                emit(Error)
            }
            .flowOn(coroutineThreadProvider.ioThread())
}

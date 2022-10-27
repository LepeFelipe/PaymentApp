package cl.flepe.payment.presentation.cardissuers

import cl.flepe.mvi.flow.ExecutionThread
import cl.flepe.payment.data.PaymentDataRepository
import cl.flepe.payment.presentation.cardissuers.CardIssuersAction.GetCardIssuersAction
import cl.flepe.payment.presentation.cardissuers.CardIssuersResult.GetCardIssuersResult
import cl.flepe.payment.presentation.cardissuers.CardIssuersResult.GetCardIssuersResult.*
import cl.flepe.payment.presentation.cardissuers.mapper.CardIssuersMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class CardIssuersProcessor @Inject constructor(
    private val repository: PaymentDataRepository,
    private val mapper: CardIssuersMapper,
    private val coroutineThreadProvider: ExecutionThread
) {
    fun actionProcessor(actions: CardIssuersAction): Flow<CardIssuersResult> =
        when (actions) {
            is GetCardIssuersAction -> cardIssuers(actions.paymentMethodId)
        }

    private fun cardIssuers(paymentMethodId: String): Flow<GetCardIssuersResult> =
        repository.getCardIssuers(paymentMethodId)
            .map { remoteCardIssuers ->
                Success(remoteCardIssuers.flatMap { remoteCardIssuer ->
                    with(mapper) {
                        remoteCardIssuer.toPresentation()
                    }
                }) as GetCardIssuersResult
            }.onStart {
                emit(InProgress)
            }.catch {
                emit(Error)
            }
            .flowOn(coroutineThreadProvider.ioThread())
}

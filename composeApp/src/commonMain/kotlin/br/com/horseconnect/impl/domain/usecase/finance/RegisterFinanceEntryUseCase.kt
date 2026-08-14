package br.com.horseconnect.impl.domain.usecase.finance

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.finance.FinanceEntryRepository
import br.com.horseconnect.impl.domain.model.FinanceEntry

class RegisterFinanceEntryUseCase(
    private val repository: FinanceEntryRepository
): BaseUseCase<FinanceEntry, Unit> {
    override suspend fun invoke(params: FinanceEntry?): ResponseResult<Unit> {
        return params?.let {
            repository.addFinanceEntry(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}

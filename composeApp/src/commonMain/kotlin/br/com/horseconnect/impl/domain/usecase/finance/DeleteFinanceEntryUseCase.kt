package br.com.horseconnect.impl.domain.usecase.finance

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.finance.FinanceEntryRepository

class DeleteFinanceEntryUseCase(
    private val repository: FinanceEntryRepository
): BaseUseCase<String, Unit> {
    override suspend fun invoke(params: String?): ResponseResult<Unit> {
        return params?.let {
            repository.deleteFinanceEntry(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}

package br.com.horseconnect.impl.domain.usecase.finance

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.finance.FinanceEntryRepository
import br.com.horseconnect.impl.domain.model.FinanceEntry

class GetFinanceEntriesUseCase(
    private val repository: FinanceEntryRepository
): BaseUseCase<Unit, List<FinanceEntry>> {
    override suspend fun invoke(params: Unit?): ResponseResult<List<FinanceEntry>> {
        return repository.getFinanceEntries()
    }
}

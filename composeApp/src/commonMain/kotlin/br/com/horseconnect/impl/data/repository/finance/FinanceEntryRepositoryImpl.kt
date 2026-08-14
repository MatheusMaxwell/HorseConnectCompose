package br.com.horseconnect.impl.data.repository.finance

import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.finance.FinanceEntryDataSource
import br.com.horseconnect.impl.domain.mapper.toData
import br.com.horseconnect.impl.domain.model.FinanceEntry

class FinanceEntryRepositoryImpl(
    private val dataSource: FinanceEntryDataSource
): FinanceEntryRepository {
    override suspend fun getFinanceEntries(): ResponseResult<List<FinanceEntry>> {
        return dataSource.getFinanceEntries(SessionManager.farm?.id.orEmpty())
    }

    override suspend fun addFinanceEntry(entry: FinanceEntry): ResponseResult<Unit> {
        return dataSource.addFinanceEntry(entry.toData())
    }

    override suspend fun updateFinanceEntry(entry: FinanceEntry): ResponseResult<Unit> {
        return dataSource.updateFinanceEntry(entry.toData(), entry.id)
    }

    override suspend fun deleteFinanceEntry(id: String): ResponseResult<Unit> {
        return dataSource.deleteFinanceEntry(id)
    }
}

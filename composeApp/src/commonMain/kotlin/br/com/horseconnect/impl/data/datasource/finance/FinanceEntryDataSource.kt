package br.com.horseconnect.impl.data.datasource.finance

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.FinanceEntryData
import br.com.horseconnect.impl.domain.model.FinanceEntry

interface FinanceEntryDataSource {

    suspend fun getFinanceEntries(farmId: String): ResponseResult<List<FinanceEntry>>
    suspend fun addFinanceEntry(entry: FinanceEntryData): ResponseResult<Unit>
    suspend fun updateFinanceEntry(entry: FinanceEntryData, entryId: String): ResponseResult<Unit>
    suspend fun deleteFinanceEntry(id: String): ResponseResult<Unit>
}

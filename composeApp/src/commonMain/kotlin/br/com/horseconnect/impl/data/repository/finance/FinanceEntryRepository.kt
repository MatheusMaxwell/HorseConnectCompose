package br.com.horseconnect.impl.data.repository.finance

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.FinanceEntry

interface FinanceEntryRepository {

    suspend fun getFinanceEntries(): ResponseResult<List<FinanceEntry>>
    suspend fun addFinanceEntry(entry: FinanceEntry): ResponseResult<Unit>
    suspend fun updateFinanceEntry(entry: FinanceEntry): ResponseResult<Unit>
    suspend fun deleteFinanceEntry(id: String): ResponseResult<Unit>
}

package br.com.horseconnect.impl.domain.usecase.pregnancy

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.pregnancy.PregnancyRepository

class DeletePregnancyUseCase(
    private val repository: PregnancyRepository
): BaseUseCase<String, Unit> {
    override suspend fun invoke(params: String?): ResponseResult<Unit> {
        return params?.let {
            repository.deletePregnancy(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}

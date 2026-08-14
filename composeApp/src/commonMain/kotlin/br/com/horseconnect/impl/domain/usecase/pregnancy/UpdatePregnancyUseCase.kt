package br.com.horseconnect.impl.domain.usecase.pregnancy

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.pregnancy.PregnancyRepository
import br.com.horseconnect.impl.domain.model.Pregnancy

class UpdatePregnancyUseCase(
    private val repository: PregnancyRepository
): BaseUseCase<Pregnancy, Unit> {
    override suspend fun invoke(params: Pregnancy?): ResponseResult<Unit> {
        return params?.let {
            repository.updatePregnancy(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}

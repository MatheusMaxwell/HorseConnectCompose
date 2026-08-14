package br.com.horseconnect.impl.domain.usecase.pregnancy

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.pregnancy.PregnancyRepository
import br.com.horseconnect.impl.domain.model.Pregnancy

class GetPregnanciesUseCase(
    private val repository: PregnancyRepository
): BaseUseCase<Unit, List<Pregnancy>> {
    override suspend fun invoke(params: Unit?): ResponseResult<List<Pregnancy>> {
        return repository.getPregnancies()
    }
}
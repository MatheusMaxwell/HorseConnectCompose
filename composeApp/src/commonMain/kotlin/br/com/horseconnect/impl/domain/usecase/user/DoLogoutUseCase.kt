package br.com.horseconnect.impl.domain.usecase.user

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.user.UserRepository

internal class DoLogoutUseCase(
    val repository: UserRepository
): BaseUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit?): ResponseResult<Unit> = repository.logout()
}

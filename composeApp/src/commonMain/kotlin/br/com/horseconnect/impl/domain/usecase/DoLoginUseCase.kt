package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.user.UserRepository
import br.com.horseconnect.impl.domain.model.User

internal class DoLoginUseCase(
    val repository: UserRepository
): BaseUseCase<DoLoginParams, User> {
    override suspend fun invoke(params: DoLoginParams?): ResponseResult<User> = params?.let {
        repository.doLogin(it.email, it.password)
    } ?: ResponseResult.Error(CommonErrors.UnknownError())
}

internal data class DoLoginParams(
    val email: String,
    val password: String
)
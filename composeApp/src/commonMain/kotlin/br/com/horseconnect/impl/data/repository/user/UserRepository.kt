package br.com.horseconnect.impl.data.repository.user

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.User


internal interface UserRepository {

    suspend fun doLogin(email: String, password: String): ResponseResult<User>

    suspend fun isAuthenticated(): Boolean

    suspend fun getCurrentUser(): User?

    suspend fun logout()
}
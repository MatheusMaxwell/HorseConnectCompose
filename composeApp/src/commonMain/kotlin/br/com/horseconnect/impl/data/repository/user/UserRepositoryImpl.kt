package br.com.horseconnect.impl.data.repository.user

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.data.datasource.user.UserDataSource
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class UserRepositoryImpl(
    val dataSource: UserDataSource,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {
    override suspend fun doLogin(email: String, password: String): ResponseResult<User> = withContext(dispatcher) {
        dataSource.doLogin(email, password)
    }
    override suspend fun isAuthenticated(): Boolean = dataSource.isAuthenticated()
    override suspend fun getCurrentUser(): User? = dataSource.getCurrentUser()?.toDomain()
    override suspend fun logout() = dataSource.logout()
}
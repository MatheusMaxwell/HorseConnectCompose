package br.com.horseconnect.data.datasource.user

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.User
import dev.gitlive.firebase.auth.FirebaseUser

internal interface UserDataSource {
    suspend fun doLogin(email: String, password: String): ResponseResult<User>

    suspend fun isAuthenticated(): Boolean

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun logout()

}
package br.com.horseconnect.impl.data.datasource.user

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.data.datasource.user.UserDataSource
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.domain.model.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

internal class UserDataSourceImpl: UserDataSource {
    override suspend fun doLogin(email: String, password: String): ResponseResult<User> {
        try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, password)
            return ResponseResult.Success(result.user!!.toDomain())
        }catch (e: Exception) {
            return ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    override suspend fun logout() {
        Firebase.auth.signOut()
    }
}
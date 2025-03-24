package br.com.horseconnect.impl.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.domain.usecase.DoLoginParams
import br.com.horseconnect.impl.domain.usecase.DoLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class LoginViewModel(
    val doLoginUseCase: DoLoginUseCase
): ViewModel() {

    private val _loginState: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState.Initial)
    val loginState: StateFlow<LoginViewState> get() = _loginState

    fun login(email: String, password: String) {
        _loginState.tryEmit(LoginViewState.Loading)
        viewModelScope.launch {
            when (val result = doLoginUseCase.invoke(DoLoginParams(email, password))) {
                is ResponseResult.Success -> result.data.run {
                    SessionManager.user = this
                    _loginState.tryEmit(
                        LoginViewState.Authenticated(this)
                    )
                }

                is ResponseResult.Error -> _loginState.tryEmit(
                    LoginViewState.Error(
                        msg = result.resultError.msg
                    )
                )
            }
        }
    }
}

internal sealed class LoginViewState() {
    data object Initial : LoginViewState()
    data object Loading : LoginViewState()
    data class Error(val msg: String) : LoginViewState()
    data class Authenticated(val user: User) : LoginViewState()
}
package br.com.horseconnect.impl.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.usecase.user.DoLogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ProfileViewModel(
    private val doLogoutUseCase: DoLogoutUseCase
): ViewModel() {

    private val _profileState: MutableStateFlow<ProfileViewState> = MutableStateFlow(ProfileViewState.Initial)
    val profileState: StateFlow<ProfileViewState> get() = _profileState

    fun logout() {
        viewModelScope.launch {
            when(doLogoutUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _profileState.tryEmit(ProfileViewState.LogoutSuccess)
                }
                is ResponseResult.Error -> {
                    _profileState.tryEmit(ProfileViewState.LogoutSuccess)
                }
            }
        }
    }
}

internal sealed class ProfileViewState {
    data object Initial: ProfileViewState()
    data object LogoutSuccess: ProfileViewState()
}

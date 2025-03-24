package br.com.horseconnect.impl.presentation.login

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.presentation.login.content.LoginContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LoginScreen(
    navigateToHome: (User) -> Unit
) {
    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginViewState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LoginContent(
        viewState = loginViewState,
        loadingDialogManager = loadingDialog,
        doLogin = {email, password ->
            loginViewModel.login(email, password)
        },
        navigateToHome = navigateToHome
    )
}


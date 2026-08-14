package br.com.horseconnect.impl.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.impl.presentation.profile.content.ProfileContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ProfileScreen(
    navigateToSplashScreen: () -> Unit
) {
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val profileViewState by profileViewModel.profileState.collectAsStateWithLifecycle()

    ProfileContent(
        viewState = profileViewState,
        user = SessionManager.user,
        farm = SessionManager.farm,
        onLogout = profileViewModel::logout,
        navigateToSplashScreen = navigateToSplashScreen
    )
}

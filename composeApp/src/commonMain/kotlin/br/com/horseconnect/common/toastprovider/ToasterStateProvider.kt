package br.com.horseconnect.common.toastprovider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val toasterStateProvider = ToasterState(
    coroutineScope = CoroutineScope(Dispatchers.Default),
)

/**
 * Provides a [Loading Dialog] that can be used by Easy application.
 */
val LocalToasterDialog = staticCompositionLocalOf {
    toasterStateProvider
}

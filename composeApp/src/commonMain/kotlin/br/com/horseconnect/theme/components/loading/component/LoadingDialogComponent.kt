package br.com.horseconnect.theme.components.loading.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun LoadingDialogComponent(
    loadingManager: LoadingDialogManager
) {
    AnimatedVisibility(visible = loadingManager.visible.value) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {
                loadingManager.text.value = ""
            }
        ) {
            Column(
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = CustomSize.loadingDialogMinHeight,
                    )
                    .width(CustomSize.loadingDialogWidth)
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(top = Padding.medium),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BaseCircularProgress()
                TextOfLoadingDialog(loadingManager.text)
            }
        }
    }
}

@Composable
private fun BaseCircularProgress(
    color: Color = HorseColor.primary
) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(CustomSize.medium),
        color = color,
        strokeWidth = CustomSize.strokeWidthProgressLoadingDialog
    )
}

@Composable
private fun TextOfLoadingDialog(
    text: MutableState<String?>
) {
    text.value?.let {
        BaseProgressText(text = it)
    } ?: Spacer(modifier = Modifier.height(CustomSize.medium))
}

@Composable
private fun BaseProgressText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.medium),
        text = text,
        textAlign = TextAlign.Center,
        color = Color.Gray
    )
}

@Preview
@Composable
private fun LoadingDialogComponentPreview() {
    LoadingDialogComponent(
        loadingManager = LoadingDialogManager(visible = mutableStateOf(true))
    )
}

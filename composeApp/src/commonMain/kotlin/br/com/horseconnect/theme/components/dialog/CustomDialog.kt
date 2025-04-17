package br.com.horseconnect.theme.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.button.SecondaryButton
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomDialog(
    visible: Boolean,
    title: String,
    message: String,
    confirmAction: ConfirmAction,
    dismissAction: DismissAction,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = dismissAction.onClick,
            title = {
                Text(text = title, style = TextStyle(fontWeight = FontWeight.Bold))
            },
            text = {
                Text(message)
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(Padding.large),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = dismissAction.text,
                        color = Color.Red,
                        modifier = Modifier.clickable {
                            dismissAction.onClick()
                            onDismiss.invoke()
                        }
                    )
                    Spacer(modifier = Modifier.width(Padding.large))
                    Text(
                        text = confirmAction.text,
                        modifier = Modifier.clickable {
                            confirmAction.onClick()
                            onDismiss.invoke()
                        }
                    )
                }
            }
        )
    }
}

data class ConfirmAction(
    val text: String,
    val onClick: () -> Unit
)

data class DismissAction(
    val text: String,
    val onClick: () -> Unit
)

@Preview
@Composable
private fun CustomDialogPreview() {
    CustomDialog(
        visible = true,
        title = "Title",
        message = "Message",
        confirmAction = ConfirmAction(
            text = "Confirm",
            onClick = {}
        ),
        dismissAction = DismissAction(
            text = "Dismiss",
            onClick = {}
        ),
        onDismiss = {}
    )
}
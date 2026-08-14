package br.com.horseconnect.theme.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    bgColor: Color = HorseColor.accent,
    textColor: Color = HorseColor.onAccent,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = Shape.medium,
        colors = ButtonDefaults.buttonColors(backgroundColor = bgColor)
    ){
        Text(
            text,
            style = TextStyle(color = textColor, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(Padding.medium)
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    HorseTheme {
        PrimaryButton("Button"){}
    }
}
package br.com.horseconnect.theme.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    bgColor: Color = HorseColor.primary,
    textColor: Color = Color.White,
    onClick: () -> Unit
){
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
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
    PrimaryButton("Button"){}
}
package br.com.horseconnect.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.shape.Shape

private val horseShapes = Shapes(
    small = Shape.small,
    medium = Shape.medium,
    large = Shape.large
)

private val horseColors = darkColors(
    primary = HorseColor.accent,
    primaryVariant = HorseColor.accent,
    onPrimary = HorseColor.onAccent,
    secondary = HorseColor.accent,
    onSecondary = HorseColor.onAccent,
    background = HorseColor.background,
    onBackground = HorseColor.textPrimary,
    surface = HorseColor.surface,
    onSurface = HorseColor.textPrimary,
    error = HorseColor.error,
    onError = HorseColor.white
)

@Composable
fun HorseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = horseColors,
        shapes = horseShapes,
        content = content
    )
}

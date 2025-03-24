package br.com.horseconnect.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.horseconnect.theme.color.HorseColor
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.icon
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val NAVIGATE_DELAY = 1000L

@Composable
fun SplashScreen(
    navigateToLogin: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(NAVIGATE_DELAY)
        navigateToLogin.invoke()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(HorseColor.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            painter = painterResource(Res.drawable.icon),
            tint = Color.White,
            contentDescription = ""
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview(){
    SplashScreen(){}
}
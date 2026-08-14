package br.com.horseconnect.impl.presentation.profile.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.impl.domain.model.Farm
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.presentation.profile.ProfileViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ProfileContent(
    viewState: ProfileViewState,
    user: User?,
    farm: Farm?,
    onLogout: () -> Unit,
    navigateToSplashScreen: () -> Unit
) {
    LaunchedEffect(viewState) {
        when(viewState) {
            ProfileViewState.Initial -> Unit
            ProfileViewState.LogoutSuccess -> navigateToSplashScreen.invoke()
        }
    }

    Scaffold(
        topBar = {
            Toolbar(title = "Perfil")
        },
        backgroundColor = HorseColor.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(Padding.x2Large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(CustomSize.profileAvatarSize)
                    .clip(Shape.circle)
                    .background(HorseColor.surface)
                    .border(BorderStroke(CustomSize.borderThin, HorseColor.border), Shape.circle),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialsOf(user?.name.orEmpty()),
                    style = TextStyle(color = HorseColor.accent, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                )
            }

            Text(
                modifier = Modifier.padding(top = Padding.large),
                text = user?.name.orEmpty(),
                style = TextStyle(color = HorseColor.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier.padding(top = Padding.small),
                text = user?.email.orEmpty(),
                style = TextStyle(color = HorseColor.textSecondary, fontSize = 13.sp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(top = Padding.x2Large),
                backgroundColor = HorseColor.surface,
                shape = Shape.medium,
                border = BorderStroke(CustomSize.borderThin, HorseColor.border),
                elevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(Padding.large)) {
                    ProfileInfoRow(label = "Haras", value = farm?.name.orEmpty())
                    ProfileInfoRow(label = "Cidade", value = "${farm?.city.orEmpty()} - ${farm?.state.orEmpty()}")
                    ProfileInfoRow(label = "Telefone", value = user?.phone.orEmpty())
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(bottom = Padding.x2Large),
                verticalArrangement = Arrangement.Bottom
            ) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onLogout.invoke() },
                    shape = Shape.medium,
                    border = BorderStroke(CustomSize.borderThin, HorseColor.error)
                ) {
                    Text(
                        text = "Sair",
                        modifier = Modifier.padding(Padding.small),
                        style = TextStyle(color = HorseColor.error, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = Padding.medium)) {
        Text(text = label, style = TextStyle(color = HorseColor.textSecondary, fontSize = 12.sp))
        Text(text = value, style = TextStyle(color = HorseColor.textPrimary, fontSize = 15.sp))
    }
}

private fun initialsOf(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts.first().take(1).uppercase()
        else -> (parts.first().take(1) + parts.last().take(1)).uppercase()
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    HorseTheme {
        ProfileContent(
            viewState = ProfileViewState.Initial,
            user = User(id = "1", name = "Matheus Meireles", email = "matheus@horseconnect.com", phone = "(11) 99999-9999"),
            farm = Farm.mock,
            onLogout = {},
            navigateToSplashScreen = {}
        )
    }
}

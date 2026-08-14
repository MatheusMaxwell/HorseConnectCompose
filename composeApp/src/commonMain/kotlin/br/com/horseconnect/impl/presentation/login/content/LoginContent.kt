package br.com.horseconnect.impl.presentation.login.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.presentation.login.LoginViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun LoginContent(
    viewState: LoginViewState,
    loadingDialogManager: LoadingDialogManager,
    doLogin: (email: String, password: String) -> Unit,
    navigateToHome: (User) -> Unit,
    navigateToSignUp: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState){
            is LoginViewState.Initial -> Unit
            is LoginViewState.Authenticated -> {
                loadingDialogManager.hide()
                navigateToHome.invoke(viewState.user)
            }
            is LoginViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
            is LoginViewState.Loading -> {
                loadingDialogManager.show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HorseColor.background)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(160.dp),
                painter = painterResource(Res.drawable.icon),
                tint = HorseColor.textPrimary,
                contentDescription = ""
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = Shape.topSheet,
            backgroundColor = HorseColor.surface,
            border = BorderStroke(CustomSize.borderThin, HorseColor.border),
            elevation = 0.dp
        ) {
            Column {
                Spacer(modifier = Modifier.height(Padding.large))
                Text(
                    modifier = Modifier.padding(horizontal = Padding.x2Large),
                    text = "Bem-vindo de volta",
                    style = TextStyle(
                        color = HorseColor.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier.padding(horizontal = Padding.x2Large, vertical = Padding.small),
                    text = "Entre para gerenciar seu haras",
                    style = TextStyle(color = HorseColor.textSecondary, fontSize = 13.sp)
                )
                Spacer(modifier = Modifier.height(Padding.medium))
                LoginTextField(
                    value = email,
                    placeholder = "Login",
                    onValueChange = {
                        email = it
                    }
                )
                LoginTextField(
                    value = password,
                    placeholder = "Senha",
                    onValueChange = {
                        password = it
                    },
                    isPassoword = true
                )
                Spacer(modifier = Modifier.height(Padding.large))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Padding.x2Large),
                    text = "Entrar"
                ){
                    doLogin.invoke(email, password)
                }
                Spacer(modifier = Modifier.height(Padding.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Não tem uma conta?",
                        style = TextStyle(color = HorseColor.textSecondary, fontSize = 13.sp)
                    )
                    TextButton(onClick = { navigateToSignUp.invoke() }) {
                        Text(
                            text = "Cadastre-se",
                            style = TextStyle(
                                color = HorseColor.accent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Padding.large))
            }
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassoword: Boolean = false
) {
    TextField(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Padding.large, vertical = Padding.small),
        value = value,
        placeholder = {
            Text(placeholder, color = HorseColor.textMuted)
        },
        onValueChange = onValueChange,
        shape = Shape.medium,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = HorseColor.surfaceVariant,
            cursorColor = HorseColor.accent,
            textColor = HorseColor.textPrimary,
            focusedIndicatorColor = HorseColor.accent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        visualTransformation = if(isPassoword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    HorseTheme {
        Box(
            modifier = Modifier.width(320.dp).height(986.dp)
        ) {
            LoginContent(
                viewState = LoginViewState.Initial,
                loadingDialogManager = LoadingDialogManager(),
                doLogin = {_, _ ->},
                navigateToHome = {_ ->}
            )
        }
    }
}

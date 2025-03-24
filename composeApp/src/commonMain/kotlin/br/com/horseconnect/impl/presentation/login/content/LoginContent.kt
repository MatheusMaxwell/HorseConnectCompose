package br.com.horseconnect.impl.presentation.login.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.presentation.login.LoginViewState
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.dimens.padding.Padding
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun LoginContent(
    viewState: LoginViewState,
    loadingDialogManager: LoadingDialogManager,
    doLogin: (email: String, password: String) -> Unit,
    navigateToHome: (User) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
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
        modifier = Modifier.fillMaxSize().background(HorseColor.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.size(250.dp).padding(top = 100.dp),
            painter = painterResource(Res.drawable.icon),
            tint = Color.White,
            contentDescription = ""
        )
        Card(
            modifier = Modifier.fillMaxHeight(0.6f).fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            backgroundColor = HorseColor.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(Padding.large))
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
                    isPassowrd = true
                )
                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    modifier = Modifier.padding(Padding.large),
                    text = "Entrar"
                ){
                    doLogin.invoke(email, password)
                }
            }
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassowrd: Boolean = false
) {
    TextField(
        modifier = Modifier.fillMaxWidth().padding(Padding.large),
        value = value,
        placeholder = {
            Text(placeholder)
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            cursorColor = Color.Black,
            disabledLabelColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        visualTransformation = if(isPassowrd) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
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
package br.com.horseconnect.theme.components.loading

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class LoadingDialogManager(
    val visible: MutableState<Boolean> = mutableStateOf(false),
    val text: MutableState<String?> = mutableStateOf(null)
) {
    fun show() {
        this.text.value = null
        this.visible.value = true
    }

    fun show(text: String) {
        this.text.value = text
        this.visible.value = true
    }

    fun hide() {
        this.visible.value = false
    }

    fun setText(text: String): LoadingDialogManager {
        this.text.value = text
        return this
    }
}

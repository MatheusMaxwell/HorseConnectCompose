package br.com.horseconnect.common.ext

import dev.gitlive.firebase.storage.Data

actual fun ByteArray.toData(): Data {
    return Data(this)
}
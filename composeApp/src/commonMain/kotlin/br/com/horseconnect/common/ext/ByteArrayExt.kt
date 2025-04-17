package br.com.horseconnect.common.ext

import coil3.Bitmap
import dev.gitlive.firebase.storage.Data

expect fun ByteArray.toData(): Data

expect fun Any.toByteArray(): ByteArray
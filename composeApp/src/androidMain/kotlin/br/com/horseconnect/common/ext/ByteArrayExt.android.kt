package br.com.horseconnect.common.ext

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import dev.gitlive.firebase.storage.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap.CompressFormat

actual fun ByteArray.toData(): Data {
    return Data(this)
}

actual fun Any.toByteArray(): ByteArray {
    require(this is Bitmap)

    val stream = ByteArrayOutputStream()
    this.compress(CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
package br.com.horseconnect.common.ext

import dev.gitlive.firebase.storage.Data
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.Foundation.*
import platform.UIKit.UIImagePNGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual fun ByteArray.toData(): Data {
    return Data(
        usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), this.size.toULong())
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
actual fun Any.toByteArray(): ByteArray {
    require(this is UIImage)

    val nsData = UIImagePNGRepresentation(this)
        ?: error("Failed to convert UIImage to PNG data")

    val byteArray = ByteArray(nsData.length.toInt())

    memScoped {
        val pointer = byteArray.refTo(0).getPointer(this)
        nsData.getBytes(pointer, nsData.length)
    }

    return byteArray
}
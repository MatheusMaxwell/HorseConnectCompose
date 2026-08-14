package br.com.horseconnect.theme.dimens.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Shape {
    val x4Small: RoundedCornerShape = RoundedCornerShape(4.dp)
    val small: RoundedCornerShape = RoundedCornerShape(8.dp)
    val medium: RoundedCornerShape = RoundedCornerShape(12.dp)
    val large: RoundedCornerShape = RoundedCornerShape(16.dp)
    val x2Large: RoundedCornerShape = RoundedCornerShape(24.dp)
    val circle: RoundedCornerShape = RoundedCornerShape(50)
    val topSheet: RoundedCornerShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
}

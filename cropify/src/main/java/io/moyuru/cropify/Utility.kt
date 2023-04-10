package io.moyuru.cropify

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

internal fun Size.toInt() = IntSize(width.toInt(), height.toInt())
internal fun Offset.toInt() = IntOffset(x.toInt(), y.toInt())
internal fun Offset.translateX(amount: Float) = copy(x = x + amount)
internal fun Offset.translateY(amount: Float) = copy(y = y + amount)
internal fun Offset.translate(amountX: Float, amountY: Float) = copy(x = x + amountX, y = y + amountY)
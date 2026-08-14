package br.com.horseconnect.common.ext

import kotlin.math.abs

fun Double.toCurrencyString(): String {
    val isNegative = this < 0
    val cents = (abs(this) * 100).toLong()
    val reais = cents / 100
    val centavos = cents % 100
    val reaisGrouped = reais.toString().reversed().chunked(3).joinToString(".").reversed()
    val centavosStr = centavos.toString().padStart(2, '0')
    val sign = if (isNegative) "-" else ""
    return "${sign}R$ $reaisGrouped,$centavosStr"
}

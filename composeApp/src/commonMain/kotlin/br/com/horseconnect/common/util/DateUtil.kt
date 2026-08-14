package br.com.horseconnect.common.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.datetime.*
import kotlin.math.min

object DateUtil {
    fun getYearsAge(value: String): Pair<Int, Int> {
        val dateTime = Instant.parse(value)
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
        val years = currentDateTime.year - date.year
        val months = currentDateTime.monthNumber - date.monthNumber
        var yearsCalculated = years
        var monthsCalculated = months

        if (months < 0) {
            yearsCalculated -= 1
            monthsCalculated += 12
        }

        return yearsCalculated to monthsCalculated
    }

    fun getMonthsAge(value: String): Int {
        val dateTime = Instant.parse(value)
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
        var months = currentDateTime.year * 12 + currentDateTime.monthNumber - (date.year * 12 + date.monthNumber)
        if (currentDateTime.dayOfMonth < date.dayOfMonth) {
            months -= 1
        }

        return months
    }

    fun getDateFormatter(value: String): String {
        if (value.isNotEmpty()) {
            val dateTime = Instant.parse(value)
            val date = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
            return "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}/${date.year}"
        } else {
            return value
        }
    }

    fun convertToISO8601(dateString: String): String {
        // Divide a entrada "dd/MM/yyyy" para obter dia, mês e ano
        val parts = dateString.split("/")
        require(parts.size == 3) { "Formato inválido, esperado dd/MM/yyyy" }

        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()

        // Cria um LocalDate com o dia, mês e ano fornecidos
        val localDate = LocalDate(year, month, day)

        // Obtemos a hora atual no UTC para criar a string ISO 8601
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val fixedTime = LocalTime(currentTime.hour, currentTime.minute, currentTime.second)

        // Combina LocalDate e LocalTime para formar LocalDateTime
        val localDateTime = LocalDateTime(localDate.year, localDate.month, localDate.dayOfMonth, fixedTime.hour, fixedTime.minute, fixedTime.second)

        // Converte para uma string no formato ISO 8601 no UTC
        return localDateTime.toInstant(TimeZone.UTC).toString()
    }

    fun formatDateString(input: String): String {
        require(input.length == 8) { "A string deve ter exatamente 8 caracteres (ddMMyyyy)" }

        // Divide a string em partes: dia, mês e ano
        val day = input.substring(0, 2)
        val month = input.substring(2, 4)
        val year = input.substring(4, 8)

        // Retorna a data formatada como dd/MM/yyyy
        return "$day/$month/$year"
    }

    fun calculateBirthForecast(date: String): String {
        val instant = Instant.parse(date)
        val dateBirth = instant.plus(340, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        val dateTime = dateBirth.toLocalDateTime(TimeZone.currentSystemDefault())

        return "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${dateTime.monthNumber.toString().padStart(2, '0')}/${dateTime.year}"
    }

    fun addDays(date: String, days: Int): String {
        val instant = Instant.parse(date)
        return instant.plus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toString()
    }

    fun addMonths(date: String, months: Int): String {
        val instant = Instant.parse(date)
        return instant.plus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault()).toString()
    }

    fun now(): String {
        return Clock.System.now().toString()
    }

    fun isCurrentMonth(date: String): Boolean {
        val target = Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault())
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return target.year == now.year && target.monthNumber == now.monthNumber
    }

    fun isPastOrToday(date: String): Boolean {
        return Instant.parse(date) <= Clock.System.now()
    }
}
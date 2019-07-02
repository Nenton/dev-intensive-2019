package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val BEFORE = " назад"
const val AFTER = "через "


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

/**
 * Метод отображения последнего визита в человекоподобном виде
 * 0с - 1с "только что"
 * 1с - 45с "несколько секунд назад"
 * 45с - 75с "минуту назад"
 * 75с - 45мин "N минут назад"
 * 45мин - 75мин "час назад"
 * 75мин 22ч "N часов назад"
 * 22ч - 26ч "день назад"
 * 26ч - 360д "N дней назад"
 * >360д "более года назад"
 */
fun Date.humanizeDiff(date: Date = Date()): String {
    var diffTime = date.time - this.time
    val isAfter = diffTime < 0
    diffTime = abs(diffTime)
    val after = if (isAfter) AFTER else ""
    val before = if (!isAfter) BEFORE else ""
    return when (diffTime) {
        in 0..SECOND -> "только что"
        in SECOND..45 * SECOND -> "несколько секунд"

        in 45 * SECOND..75 * SECOND -> after + "минуту"
        21 * MINUTE, 31 * MINUTE, 41 * MINUTE -> after + "${diffTime / MINUTE} минуту"

        in 2 * MINUTE..4 * MINUTE, in 21 * MINUTE..25 * MINUTE, in 31 * MINUTE..35 * MINUTE, in 41 * MINUTE..45 * MINUTE
        -> after + "${diffTime / MINUTE} минуты"
        in 4 * MINUTE..21 * MINUTE, in 25 * MINUTE..31 * MINUTE, in 35 * MINUTE..41 * MINUTE
        -> after + "${diffTime / MINUTE} минут"

        in 45 * MINUTE..75 * MINUTE -> after + "час"
        in 75 * MINUTE..4 * HOUR -> after + "${diffTime / HOUR} часа"
        in 4 * HOUR..20 * HOUR -> after + "${diffTime / HOUR} часов"
        in 20 * HOUR..22 * HOUR -> after + "${diffTime / HOUR} час"

        in 22 * HOUR..26 * HOUR -> after + "день"
        in 10 * DAY..15 * DAY -> after + "${diffTime / DAY} дней"
        in 26 * HOUR..360 * DAY -> {
            val dayLit = when ((diffTime / DAY) % 10) {
                1L -> "день"
                in 1L..4L -> "дня"
                else -> "дней"
            }
            after + "${diffTime / DAY} $dayLit"
        }
        else -> if (isAfter) "более чем через год" else "более года"
    } + before
}

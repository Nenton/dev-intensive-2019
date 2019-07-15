package ru.skillbranch.devintensive.extensions

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

/**
 * @author Sergey Susev
 */
class DateKtSusevTest {
    @Test
    fun humanizeDiffTest() {

        assertThat(Date().add(-0, TimeUnits.SECOND).humanizeDiff(), equalTo("только что"))
        assertThat(Date().add(-1, TimeUnits.MINUTE).humanizeDiff(), equalTo("минуту назад"))
        assertThat(Date().add(-1, TimeUnits.HOUR).humanizeDiff(), equalTo("час назад"))
        assertThat(Date().add(-1, TimeUnits.DAY).humanizeDiff(), equalTo("день назад"))

        assertThat(Date().add(-2, TimeUnits.SECOND).humanizeDiff(), equalTo("несколько секунд назад"))
        assertThat(Date().add(-2, TimeUnits.MINUTE).humanizeDiff(), equalTo("2 минуты назад"))
        assertThat(Date().add(-2, TimeUnits.HOUR).humanizeDiff(), equalTo("2 часа назад"))
        assertThat(Date().add(-2, TimeUnits.DAY).humanizeDiff(), equalTo("2 дня назад"))

        assertThat(Date().add(-5, TimeUnits.SECOND).humanizeDiff(), equalTo("несколько секунд назад"))
        assertThat(Date().add(-5, TimeUnits.MINUTE).humanizeDiff(), equalTo("5 минут назад"))
        assertThat(Date().add(-5, TimeUnits.HOUR).humanizeDiff(), equalTo("5 часов назад"))
        assertThat(Date().add(-5, TimeUnits.DAY).humanizeDiff(), equalTo("5 дней назад"))

        assertThat(Date().add(-21, TimeUnits.SECOND).humanizeDiff(), equalTo("несколько секунд назад"))
        assertThat(Date().add(-21, TimeUnits.MINUTE).humanizeDiff(), equalTo("21 минуту назад"))
        assertThat(Date().add(-21, TimeUnits.HOUR).humanizeDiff(), equalTo("21 час назад"))
        assertThat(Date().add(-21, TimeUnits.DAY).humanizeDiff(), equalTo("21 день назад"))

        assertThat(Date().add(-2, TimeUnits.HOUR).humanizeDiff(), equalTo("2 часа назад"))
        assertThat(Date().add(-5, TimeUnits.DAY).humanizeDiff(), equalTo("5 дней назад"))
        assertThat(Date().add(2, TimeUnits.MINUTE).humanizeDiff(), equalTo("через 2 минуты"))
        assertThat(Date().add(7, TimeUnits.DAY).humanizeDiff(), equalTo("через 7 дней"))
        assertThat(Date().add(-400, TimeUnits.DAY).humanizeDiff(), equalTo("более года назад"))
        assertThat(Date().add(400, TimeUnits.DAY).humanizeDiff(), equalTo("более чем через год"))
    }
}
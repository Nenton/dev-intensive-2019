package ru.skillbranch.devintensive.extensions

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class TimeUnitsTest {
    @Test
    fun pluralTest() {
        assertThat(TimeUnits.SECOND.plural(1), equalTo("1 секунду")) //1 секунду
        assertThat(TimeUnits.SECOND.plural(11), equalTo("11 секунд")) //1 секунду
        assertThat(TimeUnits.SECOND.plural(15), equalTo("15 секунд")) //1 секунду
        assertThat(TimeUnits.SECOND.plural(20), equalTo("20 секунд")) //1 секунду
        assertThat(TimeUnits.SECOND.plural(21), equalTo("21 секунду")) //1 секунду
        assertThat(TimeUnits.SECOND.plural(25), equalTo("25 секунд")) //1 секунду
        assertThat(TimeUnits.MINUTE.plural(4), equalTo("4 минуты")) //1 секунду
        assertThat(TimeUnits.MINUTE.plural(11), equalTo("11 минут")) //1 секунду
        assertThat(TimeUnits.MINUTE.plural(15), equalTo("15 минут")) //1 секунду
        assertThat(TimeUnits.HOUR.plural(19), equalTo("19 часов")) //1 секунду
        assertThat(TimeUnits.HOUR.plural(11), equalTo("11 часов")) //1 секунду
        assertThat(TimeUnits.HOUR.plural(15), equalTo("15 часов")) //1 секунду
        assertThat(TimeUnits.DAY.plural(222), equalTo("222 дня")) //1 секунду
        assertThat(TimeUnits.DAY.plural(11), equalTo("11 дней")) //1 секунду
        assertThat(TimeUnits.DAY.plural(15), equalTo("15 дней")) //1 секунду
    }
}
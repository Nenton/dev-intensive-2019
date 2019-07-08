package ru.skillbranch.devintensive.utils

import junit.framework.Assert.assertNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * @author Sergey Susev
 */
class UtilsSusevTest {
    @Test
    fun toInitialsTest() {

        assertThat(Utils.toInitials("john", "doe"), equalTo("JD"))
        assertThat(Utils.toInitials("John", null), equalTo("J"))
        assertNull(Utils.toInitials(null, null))
        assertNull(Utils.toInitials(" ", ""))
    }

    @Test
    fun transliterationTest() {
        assertThat(Utils.transliteration("Женя Стереотипов"), equalTo("Zhenya Stereotipov"))
        assertThat(Utils.transliteration("Amazing Петр", "_"), equalTo("Amazing_Petr"))
        assertThat(Utils.transliteration("[ми mi м]и", " "), equalTo("[mi mi m]i"))
    }

    @Test
    fun parseFullNameTest() {
        assertThat(Utils.parseFullName("Sergey Susev"), equalTo(Pair<String?, String?>("Sergey", "Susev")))
        assertThat(Utils.parseFullName("Sergey"), equalTo(Pair<String?, String?>("Sergey", null)))
        assertThat(Utils.parseFullName("   "), equalTo(Pair<String?, String?>(null, null)))
        assertThat(Utils.parseFullName(""), equalTo(Pair<String?, String?>(null, null)))
        assertThat(Utils.parseFullName(null), equalTo(Pair<String?, String?>(null, null)))
    }
}
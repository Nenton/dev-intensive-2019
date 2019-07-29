package ru.skillbranch.devintensive.utils

import org.intellij.lang.annotations.RegExp

object Utils {
    @RegExp
    private const val regexpUrl = "(https?://)?(www.)?(github.com)(/(?!enterprise|features|topics|collections|" +
            "trending|events|marketplace|pricing|nonprofit|customer-stories|security|login|join)\\w+)"
    private var mapLetter = mutableMapOf(
        'а' to "a",
        'б' to "b",
        'в' to "v",
        'г' to "g",
        'д' to "d",
        'е' to "e",
        'ё' to "e",
        'ж' to "zh",
        'з' to "z",
        'и' to "i",
        'й' to "i",
        'к' to "k",
        'л' to "l",
        'м' to "m",
        'н' to "n",
        'о' to "o",
        'п' to "p",
        'р' to "r",
        'с' to "s",
        'т' to "t",
        'у' to "u",
        'ф' to "f",
        'х' to "h",
        'ц' to "c",
        'ч' to "ch",
        'ш' to "sh",
        'щ' to "sh'",
        'ъ' to "",
        'ы' to "i",
        'ь' to "",
        'э' to "e",
        'ю' to "yu",
        'я' to "ya"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName == null || fullName.trim().isEmpty()) return null to null
        val parts: List<String>? = fullName.trim().split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    //    module3 FAILED java.lang.AssertionError: expected:<(null, null)> but was:<(, null)> next>>> module7 FAILED org.junit.ComparisonFailure: expected:<[mi mi m]i> but was:<[Mi M]i>
    fun transliteration(payload: String, divider: String = " "): String {
        mapLetter[' '] = divider
        var resultString = ""
        payload.toCharArray().forEach { c ->
            if (mapLetter.containsKey(c)) resultString += mapLetter[c] else {
                val lowCase = c.toLowerCase()
                if (mapLetter.containsKey(lowCase)) {
                    val case = mapLetter[lowCase]!!.replace(
                        mapLetter[lowCase]?.get(0)!!,
                        mapLetter[lowCase]?.get(0)?.toUpperCase()!!
                    )
                    resultString += case
                } else {
                    resultString += c
                }
            }
        }

        return resultString
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName == null && lastName == null) return null
        val firstLetter = firstName?.trim()?.toUpperCase()?.firstOrNull()
        val secondLetter = lastName?.trim()?.toUpperCase()?.firstOrNull()
        if (firstLetter == null && secondLetter == null) return null
        if (firstLetter == null) return secondLetter.toString()
        if (secondLetter == null) return firstLetter.toString()
        return "$firstLetter$secondLetter"
    }

    /**
     * enterprise features topics collections trending events marketplace pricing nonprofit customer-stories security login join
     */
    fun checkUrl(url: String): Boolean {
        return regexpUrl.toRegex().matches(url)
    }
}
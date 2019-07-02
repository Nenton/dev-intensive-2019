package ru.skillbranch.devintensive.utils

object Utils {
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
//        TODO FIX ME
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        mapLetter[' '] = divider
        val parseFullName = parseFullName(payload)
        var resultFirstName = ""
        var resultLastName = ""
        parseFullName.first?.toLowerCase()?.toCharArray()?.forEach { c ->
            if (mapLetter.containsKey(c)) resultFirstName += mapLetter[c] else resultFirstName += c
        }
        parseFullName.second?.toLowerCase()?.toCharArray()?.forEach { c ->
            if (mapLetter.containsKey(c)) resultLastName += mapLetter[c] else resultLastName += c
        }
        resultFirstName = resultFirstName[0].toUpperCase() + resultFirstName.substring(1)
        resultLastName = resultLastName[0].toUpperCase() + resultLastName.substring(1)
        return "$resultFirstName$divider$resultLastName"
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
}
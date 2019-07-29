package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

/**
 * @author Sergey Susev
 */
class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {

    val nickname: String = "John Doe" //TODO impl me
    val rank: String = "Junior Android Developer"
    fun toMap(): Map<String, Any> = mapOf(
        "nickName" to nickname,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )

//    Реализуй Profile.nickName как вычисляемое свойство из имени и фамилии пользователя,
//    возвращающее значение псевдонима пользователя в виде транслитерированной строки с заменой пробела на "_"
//    Пример:
//    Profile: firsName = "Женя", lastName = "Стереотипов"; Profile.nickName //Zhenya_Stereotipov
//    (Используй реализованный ранее метод Utils.transliteration)

    fun nickName() = Utils.transliteration("$firstName $lastName", "_")
}
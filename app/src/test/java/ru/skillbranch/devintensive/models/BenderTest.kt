package ru.skillbranch.devintensive.models

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * @author Susev Sergey
 */
class BenderTest {
    @Test
    fun listenAnswerPositiveTest() {
        val benderObj = Bender()

        //Как меня зовут?
        var actual = benderObj.listenAnswer("Bender").first
        var expected = "Отлично - ты справился\nНазови мою профессию?"
        assertThat(actual, equalTo(expected))

        //Мой серийный номер?
        benderObj.question = Bender.Question.SERIAL
        actual = benderObj.listenAnswer("2716057".toLowerCase()).first
        expected = "Отлично - ты справился\nНа этом все, вопросов больше нет"
        assertThat(actual, equalTo(expected))
    }

    @Test
    fun listenAnswerNegativeTest() {
        val benderObj = Bender()
        var actual = benderObj.listenAnswer("Fry")
        var expected = Pair("Это неправильный ответ\nКак меня зовут?", Bender.Status.WARNING.color)
        //Как меня зовут? #NORMAL(Triple(255, 255, 255))
        assertThat(actual.first, equalTo(expected.first))
        assertThat(actual.second, equalTo(expected.second))

        //Мой серийный номер? #CRITICAL(Triple(255, 0, 0))
        benderObj.status = Bender.Status.CRITICAL
        actual = benderObj.listenAnswer("0000000")
        expected = Pair("Это неправильный ответ. Давай все по новой\nКак меня зовут?", Bender.Status.NORMAL.color)
        assertThat(actual.first, equalTo(expected.first))
        assertThat(actual.second, equalTo(expected.second))

        //Как меня зовут? #DANGER(Triple(255, 60, 60))
        benderObj.status = Bender.Status.DANGER
        actual = benderObj.listenAnswer("Fry")
        expected = Pair("Это неправильный ответ\nКак меня зовут?", Bender.Status.CRITICAL.color)
        assertThat(actual.first, equalTo(expected.first))
        assertThat(actual.second, equalTo(expected.second))
    }

    @Test
    fun validateValueTest() {
        val benderObj = Bender()
        //Как меня зовут? #NORMAL(Triple(255, 255, 255))
        //Имя должно начинаться с заглавной буквы\nКак меня зовут? #NORMAL(Triple(255, 255, 255))
        var actual = benderObj.listenAnswer("bender")
        var expected = Pair("Имя должно начинаться с заглавной буквы\nКак меня зовут?", Bender.Status.NORMAL.color)
        assertThat(actual.first, equalTo(expected.first))
        assertThat(actual.second, equalTo(expected.second))


        benderObj.status = Bender.Status.NORMAL
        benderObj.question = Bender.Question.SERIAL
        actual = benderObj.listenAnswer("2716057")
        assertThat(actual.first, equalTo("Отлично - ты справился\nНа этом все, вопросов больше нет"))
        assertThat(actual.second, equalTo(Bender.Status.NORMAL.color))

        //Отлично - ты справился\nНа этом все, вопросов больше нет #NORMAL(Triple(255, 255, 255))
        //На этом все, вопросов больше нет #NORMAL(Triple(255, 255, 255))
        actual = benderObj.listenAnswer("any text")
        expected = Pair("На этом все, вопросов больше нет", Bender.Status.NORMAL.color)
        assertThat(actual.first, equalTo(expected.first))
        assertThat(actual.second, equalTo(expected.second))

        assertThat(Bender.Question.NAME.validateValue("bender"), equalTo(true))
        assertThat(Bender.Question.PROFESSION.validateValue("Bender"), equalTo(true))
        assertThat(Bender.Question.MATERIAL.validateValue("металл35"), equalTo(true))
        assertThat(Bender.Question.BDAY.validateValue("24абв14"), equalTo(true))
        assertThat(Bender.Question.SERIAL.validateValue("000000"), equalTo(true))
    }
}
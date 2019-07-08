package ru.skillbranch.devintensive.extensions

import org.junit.Test

class StringTest {
    @Test
    fun truncateTest() {
        println("Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate()) //Bender Bending R...
        println("Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(15)) //Bender Bending...
        println("Bender Bending            ".truncate(15)) //Bender Bending...
        println("A     ".truncate(3)) //Bender Bending R...
        //A
    }

    @Test
    fun stripHtmlTest() {
        println("<p class=\"title\">Образовательное IT-сообщество Skill Branch</p>".stripHtml()) //Образовательное IT-сообщество Skill Branch
        println("<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml()) //Образовательное IT-сообщество Skill Branch
    }
}
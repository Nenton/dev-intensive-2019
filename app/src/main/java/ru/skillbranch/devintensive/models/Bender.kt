package ru.skillbranch.devintensive.models

/**
 * @author Sergey Susev
 */
class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    /**
     * Реализуй метод listenAnswer с сигнатурой listenAnswer(answer: String): Pair>.
     * Вопросы и ответы класса Bender, а также значения цветов статусов, прикреплены к ресурсам урока
     *
     * Требования к методу:
     * При вводе верного ответа изменить текущий вопрос на следующий вопрос (question = question.nextQuestion())
     * и вернуть "Отлично - ты справился\n${question.question}" to status.color
     * Если вопросы закончились (Question.IDLE), вернуть "Отлично - ты справился\nНа этом все, вопросов больше нет"
     * Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity
     * (достаточно сохранить Status, Question)
     *
     * При вводе неверного ответа изменить текущий статус на следующий статус (status = status.nextStatus()),
     * вернуть "Это неправильный ответ\n${question.question}" to status.color и изменить цвет
     * ImageView (iv_bender) на цвет status.color (метод setColorFilter(color,"MULTIPLY"))
     * При вводе неверного ответа более 3 раз сбросить состояние сущности Bender на значение по умолчанию
     * (status = Status.NORMAL, question = Question.NAME) и вернуть
     * "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
     * и изменить цвет ImageView (iv_bender) на цвет status.color
     * Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity
     * (достаточно сохранить Status, Question)
     *
     * Пример:
     * //Как меня зовут?
     * benderObj.listenAnswer("Bender") //Отлично - ты справился\nНазови мою профессию?
     * //Мой серийный номер?
     * benderObj.listenAnswer("2716057") //Отлично - ты справился\nНа этом все, вопросов больше нет
     * //Как меня зовут?
     * benderObj.listenAnswer("Bender") //Отлично - ты справился\nНазови мою профессию?
     * //onPause() -> onStop() -> onDestroy() -> onCreate()
     * //Назови мою профессию?
     *
     * Пример:
     * //Как меня зовут? #NORMAL(Triple(255, 255, 255))
     * benderObj.listenAnswer("Fry") //Это неправильный ответ\nКак меня зовут? #WARNING(Triple(255, 120, 0))
     * //Мой серийный номер? #CRITICAL(Triple(255, 0, 0))
     * benderObj.listenAnswer("0000000") //Это неправильный ответ. Давай все по новой\nКак меня зовут? #NORMAL(Triple(255, 255, 255))
     * //Как меня зовут? #DANGER(Triple(255, 60, 60))
     * benderObj.listenAnswer("Fry") //Это неправильный ответ\nКак меня зовут? #CRITICAL(Triple(255, 0, 0))
     * //onPause() -> onStop() -> onDestroy() -> onCreate()
     * //Как меня зовут? #CRITICAL(Triple(255, 0, 0))
     */
    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        if (question.answers.isEmpty()) return question.question to status.color
        return if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            badAnswer(answer)
        }
    }

    private fun badAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.validateValue(answer)) {
            val result = question.validateMessage() +
                    (if (question.validateMessage().isEmpty()) "" else "\n") +
                    question.question

            result to status.color
        } else {
            val result = "Это неправильный ответ" +
                    (if (status == Status.CRITICAL) ". Давай все по новой" else "") +
                    "\n${question.question}"
            status = status.nextStatus()
            result to status.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (ordinal < values().lastIndex) {
                values()[ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun validateValue(value: String): Boolean {
                for (answer in answers) {
                    if (answer.toLowerCase() == value) return true
                }
                return false
            }

            override fun validateMessage(): String = "Имя должно начинаться с заглавной буквы"

            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validateValue(value: String): Boolean = answers.contains(value.toLowerCase())

            override fun validateMessage(): String = "Профессия должна начинаться со строчной буквы"

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validateValue(value: String): Boolean {
                for (c in value.toCharArray()) {
                    if (c.isDigit()) return true
                }
                return false
            }

            override fun validateMessage(): String = "Материал не должен содержать цифр"

            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validateValue(value: String): Boolean {
                for (c in value.toCharArray()) {
                    if (!c.isDigit()) return true
                }
                return false
            }

            override fun validateMessage(): String = "Год моего рождения должен содержать только цифры"

            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validateValue(value: String): Boolean {
                if (value.length != 7) return true
                for (c in value.toCharArray()) {
                    if (!c.isDigit()) return true
                }
                return false
            }

            override fun validateMessage(): String = "Серийный номер содержит только цифры, и их 7"

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validateValue(value: String): Boolean = true
            override fun validateMessage(): String = ""
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
        /**
         * Реализуй проверку вводимых пользователем ответов на соответствие условиям валидации для каждого типа вопроса (валидация НЕ влияет на Status)
         * Question.NAME -> "Имя должно начинаться с заглавной буквы"
         * Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
         * Question.MATERIAL -> "Материал не должен содержать цифр"
         * Question.BDAY -> "Год моего рождения должен содержать только цифры"
         * Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
         * Question.IDLE -> //игнорировать валидацию
         * Пример:
         * //Как меня зовут? #NORMAL(Triple(255, 255, 255))
         * benderObj.listenAnswer("bender") //Имя должно начинаться с заглавной буквы\nКак меня зовут? #NORMAL(Triple(255, 255, 255))
         * //Отлично - ты справился\nНа этом все, вопросов больше нет #NORMAL(Triple(255, 255, 255))
         * benderObj.listenAnswer("any text") //На этом все, вопросов больше нет #NORMAL(Triple(255, 255, 255))
         */
        abstract fun validateValue(value: String): Boolean

        abstract fun validateMessage(): String
    }
}
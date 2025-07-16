package com.example.gymappia.model

enum class QuestionType {
    MultipleChoice, NumberResponse, SingleChoice, StringResponse
}

sealed class Question(
    open val questionText: String,
    open val possibleAnswerChoices: List<String>?,
    open val type: QuestionType
) {

    data class StringResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.StringResponse
    ): Question(questionText, possibleAnswerChoices = null, type)

    data class MultiChooseQuestion(
        override val questionText: String,
        override val possibleAnswerChoices: List<String>,
        override val type: QuestionType = QuestionType.MultipleChoice
    ) : Question(questionText, possibleAnswerChoices, type)

    data class SingleChooseQuestion(
        override val questionText: String,
        override val possibleAnswerChoices: List<String>,
        override val type: QuestionType = QuestionType.SingleChoice
    ) : Question(questionText, possibleAnswerChoices, type)

    data class NumberResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.NumberResponse
    ): Question(questionText = questionText, type = type, possibleAnswerChoices = null)

}
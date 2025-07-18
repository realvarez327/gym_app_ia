package com.example.gymappia.model

enum class QuestionType {
    MultipleChoice, NumberResponse, SingleChoice, StringResponse
}

enum class NumberQuestionSubject{
    Weight, Height, Age
}

sealed class Question(
    open val questionText: String,
    open val possibleAnswerChoices: List<String>?,
    open val type: QuestionType,
    open val numberQuestionSubject: NumberQuestionSubject?
) {

    data class StringResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.StringResponse
    ): Question(questionText, possibleAnswerChoices = null, type, numberQuestionSubject = null)

    data class MultiChooseQuestion(
        override val questionText: String,
        override val possibleAnswerChoices: List<String>,
        override val type: QuestionType = QuestionType.MultipleChoice
    ) : Question(questionText, possibleAnswerChoices, type, numberQuestionSubject = null)

    data class SingleChooseQuestion(
        override val questionText: String,
        override val possibleAnswerChoices: List<String>,
        override val type: QuestionType = QuestionType.SingleChoice
    ) : Question(questionText, possibleAnswerChoices, type, numberQuestionSubject = null)

    data class NumberResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.NumberResponse,
        override val numberQuestionSubject: NumberQuestionSubject
    ): Question(questionText = questionText, type = type, possibleAnswerChoices = null, numberQuestionSubject = numberQuestionSubject)

}
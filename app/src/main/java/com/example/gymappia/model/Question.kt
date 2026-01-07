package com.example.gymappia.model

enum class QuestionType {
    MultipleChoice, NumberResponse, SingleChoice, StringResponse
}

enum class NumberQuestionSubject{
    Weight, Height, Age
}

enum class SingleChoiceQuestionSubject{
    Gender, Notifications
}
sealed class Question(
    open val questionText: String,
    open val possibleAnswerChoices: List<Any>?,
    open val possibleGoalChoices: List<FitnessGoal>?,
    open val type: QuestionType,
    open val numberQuestionSubject: NumberQuestionSubject?,
    open val singleChooseSubject: SingleChoiceQuestionSubject?
) {

    data class StringResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.StringResponse
    ): Question(
        questionText,
        possibleAnswerChoices = null,
        type = type,
        numberQuestionSubject = null,
        singleChooseSubject = null,
        possibleGoalChoices = null
    )

    data class MultiChooseQuestion(
        override val questionText: String,
        override val possibleGoalChoices: List<FitnessGoal>?,// does this have to be nullable
        override val type: QuestionType = QuestionType.MultipleChoice
    ) : Question(
        questionText,
        possibleGoalChoices = possibleGoalChoices,
        type =type,
        numberQuestionSubject = null,
        singleChooseSubject = null,
        possibleAnswerChoices = null)


    data class SingleChooseQuestion(
        override val questionText: String,
        override val possibleAnswerChoices: List<Any>,
        override val type: QuestionType = QuestionType.SingleChoice,
        override val singleChooseSubject: SingleChoiceQuestionSubject
    ) : Question(
        questionText,
        possibleAnswerChoices,
        type= type,
        possibleGoalChoices = null,
        singleChooseSubject = singleChooseSubject,
        numberQuestionSubject = null
    )

    data class NumberResponseQuestion(
        override val questionText: String,
        override val type: QuestionType = QuestionType.NumberResponse,
        override val numberQuestionSubject: NumberQuestionSubject
    ): Question(
        questionText = questionText,
        type = type,
        possibleAnswerChoices = null,
        numberQuestionSubject = numberQuestionSubject,
        singleChooseSubject = null,
        possibleGoalChoices = null)

}
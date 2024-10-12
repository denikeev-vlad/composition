package com.example.composition.domain.game

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase

class GameLogic(
    private val gameSettings: GameSettings,
    private val generateQuestionUseCase: GenerateQuestionUseCase
) {
    var countOfRightAnswers = 0
    var countOfQuestions = 0

    fun generateQuestion(): Question {
        return generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun checkAnswer(question: Question, answer: Int): Boolean {
        val isCorrect = question.rightAnswer == answer
        if (isCorrect) countOfRightAnswers++
        countOfQuestions++
        return isCorrect
        generateQuestion()
    }

    fun calculatePercentRightAnswers(): Int {
        if (countOfQuestions == 0) {
            return 0
        }
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }


}

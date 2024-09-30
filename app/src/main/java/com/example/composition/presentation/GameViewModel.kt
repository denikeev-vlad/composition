package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.game.GameLogic
import com.example.composition.domain.game.GameTimer
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingUseCase

class GameViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    private var timer: GameTimer? = null
    private lateinit var gameLogic: GameLogic

    private val context = application

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingUseCase = GetGameSettingUseCase(repository)

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question : LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers : LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateNextQuestion()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingUseCase(level)
        gameLogic = GameLogic(gameSettings, generateQuestionUseCase)
    }

    private fun startTimer() {
        timer = GameTimer(gameSettings.gameTimeInSeconds, { time ->
            _formattedTime.value = time
        }, {
            finishGame()
        })
        timer?.start()
    }

    private fun generateNextQuestion() {
        _question.value = gameLogic.generateQuestion()
    }

    fun chooseAnswer(answer: Int) {
        val currentQuestion = _question.value ?: return
        val isCorrect = gameLogic.checkAnswer(currentQuestion, answer)
        updateProgress()
        generateNextQuestion()
    }

    private fun updateProgress() {
        val percent = gameLogic.calculateProgress()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            gameLogic.countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
    }

    private fun finishGame() {
        val result = GameResult(
            gameLogic.countOfRightAnswers >= gameSettings.minCountOfRightAnswers,
            gameLogic.countOfRightAnswers,
            gameLogic.countOfQuestions,
            gameSettings
        )
        _gameResult.value = result
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
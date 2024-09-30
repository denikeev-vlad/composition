package com.example.composition.domain.game

import android.os.CountDownTimer

class GameTimer(
    private val gameTimeInSeconds: Long,
    private val onTick: (String) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null

    fun start() {
        timer = object : CountDownTimer(gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    fun cancel() {
        timer?.cancel()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds % SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}
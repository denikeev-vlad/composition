package com.example.composition.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameFinishedViewModel : ViewModel() {

    private val _requiredAnswers = MutableLiveData<Boolean>()
    val requiredAnswers : LiveData<Boolean>
        get() = _requiredAnswers

}
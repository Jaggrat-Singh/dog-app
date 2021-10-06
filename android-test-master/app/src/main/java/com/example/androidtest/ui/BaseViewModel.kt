package com.example.androidtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.observable

abstract class BaseViewModel<T : Any>(initialState: T) : ViewModel() {

    protected var viewState: T by observable(initialState) { _, _, newState ->
        viewStateLiveData.value = newState
    }

    private val viewStateLiveData = MutableLiveData<T>()

    init {
        viewStateLiveData.value = initialState
    }

    fun getViewState(): LiveData<T> = viewStateLiveData
}
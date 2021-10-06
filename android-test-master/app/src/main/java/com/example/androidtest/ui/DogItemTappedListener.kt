package com.example.androidtest.ui

import com.example.androidtest.models.DogUIModel

interface DogItemTappedListener  {
    fun onItemTapped(mode: DogUIModel)
}
package com.example.androidtest.models

/**
 * Send only information that is needed by UI rather than sending all fields from API response.
 * */
data class DogUIModel (val name: String, val imageUrl: String, val lifeSpan: String, val temperament: String?)
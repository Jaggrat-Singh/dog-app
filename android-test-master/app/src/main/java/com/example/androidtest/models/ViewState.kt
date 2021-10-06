package com.example.androidtest.models

/**
 * It shows how many events are supported by view model
 */
sealed class ViewEvents { 
    object ScreenLoad : ViewEvents()
    object FetchAllDogData : ViewEvents()
    data class UserSearch(val query: String) : ViewEvents()
    data class ItemTapped(val uiModel: DogUIModel) : ViewEvents()
}

/**
 * It shows what is the state of the UI at any point of time.
 */
sealed class DogViewState {
    object InIt: DogViewState()
    object Loading : DogViewState()
    data class Error(val error: String): DogViewState()
    data class Success(val results: List<DogUIModel>): DogViewState()
    data class ItemTappedResult(val breed: Breed): DogViewState()
    object NoResultFound: DogViewState()
}
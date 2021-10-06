package com.example.androidtest.ui

import androidx.lifecycle.MutableLiveData
import com.example.androidtest.models.Breed
import com.example.androidtest.models.DogUIModel
import com.example.androidtest.models.DogViewState
import com.example.androidtest.models.ViewEvents
import com.example.androidtest.repo.Repo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

public class MainViewModel @Inject constructor(private val repo: Repo) : BaseViewModel<DogViewState>(DogViewState.InIt) {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val currentData: MutableLiveData<List<Breed>> = MutableLiveData()

    /**
     * 1. It is a single point of access to outer world to do its task.
     * 2. Wanted to showcase uni directional data flow
     * 3. DogViewState will be updated to notify activity to do it's work.
     * @param events: select one of the supported type
     */
    fun processEvent(events: ViewEvents) {
        when(events) {
            is ViewEvents.FetchAllDogData -> {
                fetchDogList()
            }
            is ViewEvents.ScreenLoad -> {
                fetchDogList()
            }
            is ViewEvents.UserSearch -> {
                searchForDog(events.query)
            }
            is ViewEvents.ItemTapped -> {
                showDetail(events.uiModel)
            }
        }
    }

    private fun showDetail(dogUIModel: DogUIModel) {
        if (currentData.value.isNullOrEmpty().not()) {
            val item = currentData.value!!.find { it.name == dogUIModel.name }
            if(item != null) {
                viewState = DogViewState.ItemTappedResult(item)
            }
        }
    }

    /**
     * Searches for the currently visible list if any, otherwise calls API(with query param) to get the data.
     * TODO If I had more time I would have stored data in sqlite
     */
    private fun searchForDog(keyword: String) {
        if(keyword.isBlank()) {
            viewState = DogViewState.Error("Wrong Keyword")
        } else if (currentData.value.isNullOrEmpty().not()) {
            val item = currentData.value!!.find { it.name.contains(keyword, ignoreCase = true) }
            if(item != null) {
                currentData.postValue(listOf(item))
                viewState = DogViewState.Success(processDogResponse(listOf(item)))
            } else {
                searchDog(keyword)
            }
        } else {
            searchDog(keyword)
        }
    }

   private fun searchDog(keyword: String) {
       val searchDisposable = repo.searchDog(keyword).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
           .subscribe({ templates ->
               val list = templates.body() ?: listOf()
               viewState = if (list.isEmpty()) {
                   DogViewState.NoResultFound
               } else {
                   val uiModels = processDogResponse(list)
                   currentData.postValue(list)
                   DogViewState.Success(uiModels)
               }

           }, {
               viewState = DogViewState.Error(it.localizedMessage)
           })
        disposable.add(searchDisposable)
    }

    private fun fetchDogList() {
        viewState = DogViewState.Loading
        val dogDisposable = repo.getDogList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ templates ->
                val list = templates.body() ?: listOf()
                val uiModels = processDogResponse(list)
                viewState = DogViewState.Success(uiModels)
                currentData.postValue(list)
            }, {
                viewState = DogViewState.Error(it.localizedMessage)
            })
        disposable.add(dogDisposable)
    }

    /**
     * UI might not need all the fields from API response so tailoring it as per UI need
     */
    private fun processDogResponse(list: List<Breed>): List<DogUIModel> {
        val dogUIModelList: ArrayList<DogUIModel> = arrayListOf()
        list.forEach {

            val imageUrl = "https://cdn2.thedogapi.com/images/${it.referenceImageId}.jpg"

            dogUIModelList.add(DogUIModel(it.name, imageUrl, "Age " + it.lifeSpan, it.temperament))

        }
        return dogUIModelList
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
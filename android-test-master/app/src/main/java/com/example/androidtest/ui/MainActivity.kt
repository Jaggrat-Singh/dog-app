package com.example.androidtest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtest.R
import com.example.androidtest.di.Injectable
import com.example.androidtest.models.DogUIModel
import com.example.androidtest.models.DogViewState
import com.example.androidtest.models.ViewEvents
import com.example.androidtest.ui.adapter.DogListAdapter
import com.example.androidtest.utils.DATA_KEY
import com.example.androidtest.utils.verifyAvailableNetwork
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable, DogItemTappedListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val adapter = DogListAdapter(this)

    private val viewEvent: LiveData<ViewEvents> get() = _viewEvent
    private val _viewEvent = MutableLiveData<ViewEvents>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)

        setupView()
        viewModel.getViewState().observe(this, Observer { render(it) })

        /**
         * It is single point of access to viewmodel as I don't want to flood UI with viewmodel references. Wanted to maintain uni-directonal data flow
         */
        viewEvent.observe(this, Observer { event ->
            viewModel.processEvent(event)
        })

        // check if network is available to load data.
        if (savedInstanceState == null && verifyAvailableNetwork(this)) {
            _viewEvent.postValue(ViewEvents.ScreenLoad)
        } else {
            Toast.makeText(this, getString(R.string.internet_warning), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Renders each state on UI
     */
    private fun render(viewState: DogViewState) {
        when(viewState) {
            is DogViewState.InIt -> Toast.makeText(
                this,
                getString(R.string.welcome_message),
                Toast.LENGTH_LONG
            ).show()
            is DogViewState.Loading -> showProgressing()
            is DogViewState.Success -> {
                adapter.setDogUIData(viewState.results)
                dismissProgressing()
                recycler_view.visibility = View.VISIBLE
            }
            is DogViewState.NoResultFound -> {
                Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
                dismissProgressing()
                recycler_view.visibility = View.GONE
            }
            is DogViewState.Error -> {
                Toast.makeText(this, getString(R.string.api_error), Toast.LENGTH_LONG).show()
                dismissProgressing()
                recycler_view.visibility = View.GONE
            }
            is DogViewState.ItemTappedResult -> {
                Intent(applicationContext, DetailActivity::class.java).also {
                    it.putExtra(DATA_KEY, viewState.breed)
                    startActivity(it)
                }
            }
        }
    }

    /**
     * Sets up views
     */
    private fun setupView() {
        btn_search.setOnClickListener {
            _viewEvent.postValue(ViewEvents.UserSearch(edt_keyword.text.toString()))
        }

        btn_fetch_all.setOnClickListener {
            _viewEvent.postValue(ViewEvents.FetchAllDogData)
        }
        recycler_view.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(it.context, RecyclerView.VERTICAL))
        }
    }

    /**
     * Show progress
     */
    private fun showProgressing() {
        LoadingDialogFragment().show(supportFragmentManager, LoadingDialogFragment.TAG)
    }

    /**
     * Dismiss progress
     */
    private fun dismissProgressing() {
        supportFragmentManager.findFragmentByTag(LoadingDialogFragment.TAG)
            ?.takeIf { it is DialogFragment }
            ?.run {
                (this as DialogFragment).dismiss()
            }
    }

    override fun onItemTapped(model: DogUIModel) {
        _viewEvent.postValue(ViewEvents.ItemTapped(model))
    }
}

package com.example.androidtest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidtest.models.Breed
import com.example.androidtest.models.DogUIModel
import com.example.androidtest.models.DogViewState
import com.example.androidtest.models.ViewEvents
import com.example.androidtest.repo.Repo
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


class MainViewModelTest {

    private lateinit var repo: Repo
    private lateinit var viewModel: MainViewModel

    @get:Rule val rule = InstantTaskExecutorRule()
    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()
    val mockBreed = Breed(1, "name", "breed for","breed ground", "lifespan","temera","origin","UK","askdjalsd")
    val mockForNewBreed = Breed(1, "hello there", "breed for","breed ground", "lifespan","temera","origin","UK","askdjalsd")
    val mockDogUIModel = DogUIModel(name="name", imageUrl="https://cdn2.thedogapi.com/images/askdjalsd.jpg", lifeSpan="Age lifespan", temperament="temera")
    val mockNewDogUIModel = DogUIModel(name="hello there", imageUrl="https://cdn2.thedogapi.com/images/askdjalsd.jpg", lifeSpan="Age lifespan", temperament="temera")
    @Before
    fun setUp() {
        repo = mock()
        viewModel = MainViewModel(repo)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun `When screen is loaded then list of dogs is shown`() {
        // given
        whenever(repo.getDogList()).thenReturn(getMockDodResponse())

        // when
        viewModel.processEvent(ViewEvents.ScreenLoad)

        //then
        verify(repo, times(1)).getDogList()
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.Success(listOf(mockDogUIModel)))
    }

    @Test
    fun `When user search for dog name then UI shows results without making api call since data is available`() {
        // given
        whenever(repo.getDogList()).thenReturn(getMockDodResponse())
        viewModel.processEvent(ViewEvents.ScreenLoad)


        // when
        viewModel.processEvent(ViewEvents.UserSearch("name"))

        //then
        verify(repo, times(0)).searchDog("name")
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.Success(listOf(mockDogUIModel)))
    }

    @Test
    fun `When user search for dog name then UI shows results given after making API call since list does not have data`() {
        // given
        whenever(repo.searchDog("hello there")).thenReturn(getMockDodResponse(mockForNewBreed))
        whenever(repo.getDogList()).thenReturn(getMockDodResponse(mockBreed))
        viewModel.processEvent(ViewEvents.ScreenLoad)

        // when
        viewModel.processEvent(ViewEvents.UserSearch("hello there"))

        //then
        verify(repo, times(1)).searchDog("hello there")
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.Success(listOf(mockNewDogUIModel)))
    }

    @Test
    fun `When user's keyword is invalid then UI shows Error`() {
        // given
        whenever(repo.getDogList()).thenReturn(getMockDodResponse(mockBreed))
        viewModel.processEvent(ViewEvents.ScreenLoad)

        // when
        viewModel.processEvent(ViewEvents.UserSearch("  "))

        //then
        verify(repo, times(0)).searchDog(any())
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.Error("Wrong Keyword"))
    }

    @Test
    fun `When user's keyword has not found any result from API call`() {
        // given
        whenever(repo.searchDog("bla bla")).thenReturn(Single.just(Response.success(listOf())))

        // when
        viewModel.processEvent(ViewEvents.UserSearch("bla bla"))

        //then
        verify(repo, times(1)).searchDog(any())
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.NoResultFound)
    }

    @Test
    fun `When user selects an item then UI shows detail page`() {
        // given
        whenever(repo.getDogList()).thenReturn(getMockDodResponse(mockBreed))
        viewModel.processEvent(ViewEvents.ScreenLoad)

        // when
        viewModel.processEvent(ViewEvents.ItemTapped(mockDogUIModel))

        //then
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.ItemTappedResult(mockBreed))
    }

    @Test
    fun `When user selects Get All event then UI should list to user`() {
        // given
        whenever(repo.getDogList()).thenReturn(getMockDodResponse(mockBreed))

        // when
        viewModel.processEvent(ViewEvents.FetchAllDogData)

        //then
        verify(repo, times(1)).getDogList()
        TestCase.assertEquals(viewModel.getViewState().value, DogViewState.Success(listOf(mockDogUIModel)))
    }


    private fun getMockDodResponse(model: Breed = mockBreed) = Single.just(Response.success(listOf(model)))
}
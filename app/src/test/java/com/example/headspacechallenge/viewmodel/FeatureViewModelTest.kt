package com.example.headspacechallenge.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.data.repository.FeatureRepository
import com.example.headspacechallenge.database.DatabaseProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.net.UnknownHostException

@RunWith(BlockJUnit4ClassRunner::class)
class FeatureViewModelTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var viewModel: FeatureViewModel
    @MockK
    lateinit var featureRepository: FeatureRepository
    @MockK
    lateinit var compositeDisposable: CompositeDisposable
    @RelaxedMockK
    lateinit var application: Application


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FeatureViewModel(featureRepository,compositeDisposable,application)
        every { compositeDisposable.add(any()) } returns true
    }

    @Test
    fun getPics_WhenRepositoryReturnNonEmptyList(){

        val items = listOf(
            FeatureModel("Mubarak","dummyDownloadURL",56,"dummyId","dummyURL",56),
            FeatureModel("Mubarak2","dummyDownloadURL2",56,"dummyId","dummyURL",56),
            FeatureModel("Mubarak3","dummyDownloadURL3",56,"dummyId","dummyURL",56)
        )

        every { featureRepository.getPics() } returns Single.just( items)

        viewModel.getPicsFromAPI()

        assertEquals(items,viewModel.items.value)
        assertEquals(null,viewModel.errorMessage.value)
        assertEquals(FeatureViewModel.LoadingState.SUCCESS,viewModel.loadingState.value)
    }

    @Test
    fun getPicShowError_WhenRepositoryReturnsEmptyList(){

        every { featureRepository.getPics() } returns Single.just( emptyList())

        viewModel.getPicsFromAPI()

        assertEquals(null,viewModel.items.value)
        assertEquals("No Photo Found",viewModel.errorMessage.value)
        assertEquals(FeatureViewModel.LoadingState.ERROR,viewModel.loadingState.value)
    }

    @Test
    fun getPicsShowsNetworkError_WhenRepositoryReturnsUnknownHostException(){
        every { featureRepository.getPics() } returns Single.error(UnknownHostException())
        viewModel.getPicsFromAPI()
        assertEquals(null,viewModel.items.value)
        assertEquals("Network Error Occurred",viewModel.errorMessage.value)
        assertEquals(FeatureViewModel.LoadingState.ERROR,viewModel.loadingState.value)
    }

    @Test
    fun getPicsShowLocalizedError_WhenRepositoryReturnsOthersTypeOfException(){
        every { featureRepository.getPics() } returns Single.error(RuntimeException("This is a custom exception"))
        viewModel.getPicsFromAPI()
        assertEquals(null,viewModel.items.value)
        assertEquals("This is a custom exception",viewModel.errorMessage.value)
        assertEquals(FeatureViewModel.LoadingState.ERROR,viewModel.loadingState.value)
    }

}
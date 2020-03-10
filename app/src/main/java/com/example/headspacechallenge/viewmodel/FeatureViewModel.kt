package com.example.headspacechallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.data.repository.FeatureRepository
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class FeatureViewModel(private val featureRepository: FeatureRepository,
                       private val disposable: CompositeDisposable
): ViewModel() {


    fun getPicsFromAPI() {
        loadingState.value = LoadingState.LOADING
        disposable.add(
            featureRepository.getPics().subscribe({ result ->
                //In case of empty list of items show error message
                if (result.isEmpty()) {
                    errorMessage.value = "No Photo Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    items.value = result
                    loadingState.value = LoadingState.SUCCESS

                }
            }, {
                when (it) {
                    is UnknownHostException -> errorMessage.value = "Network Error Occurred"
                    else -> errorMessage.value = it.localizedMessage
                }

                loadingState.value = LoadingState.ERROR
            }
            )
        )
    }

    val items: MutableLiveData<List<FeatureModel>> = MutableLiveData()

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}
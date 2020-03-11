package com.example.headspacechallenge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.data.repository.FeatureRepository
import com.example.headspacechallenge.database.DatabaseProvider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class FeatureViewModel(
    private val featureRepository: FeatureRepository,
    private val disposable: CompositeDisposable,
    application: Application
) : AndroidViewModel(application) {

    var database = DatabaseProvider.provideRoomDatabase(application)

    val items: MutableLiveData<List<FeatureModel>> = MutableLiveData()

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

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
                    insert(result)

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

    fun insert(items: List<FeatureModel>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                database.featureDAO().insertAll(items)
            }
        }
    }

    fun retrieveItemsFromDB() {
        var pictures = listOf<FeatureModel>()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                pictures = database.featureDAO().getAll()
            }
        }
        items.value = pictures
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
package com.example.headspacechallenge.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.headspacechallenge.data.repository.FeatureRepository
import io.reactivex.disposables.CompositeDisposable

class FeatureViewModelFactory constructor(private val featureRepository: FeatureRepository, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeatureViewModel(featureRepository, CompositeDisposable(),application) as T
    }
}
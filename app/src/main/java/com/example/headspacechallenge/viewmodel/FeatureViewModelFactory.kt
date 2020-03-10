package com.example.headspacechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.headspacechallenge.data.repository.FeatureRepository
import io.reactivex.disposables.CompositeDisposable

class FeatureViewModelFactory constructor(private val featureRepository: FeatureRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeatureViewModel(featureRepository, CompositeDisposable()) as T
    }
}
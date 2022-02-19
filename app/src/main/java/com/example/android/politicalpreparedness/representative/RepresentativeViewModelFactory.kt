package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import kotlin.coroutines.CoroutineContext

class RepresentativeViewModelFactory(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider,
    private val coroutineContext: CoroutineContext
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(politicalPreparednessProvider, coroutineContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

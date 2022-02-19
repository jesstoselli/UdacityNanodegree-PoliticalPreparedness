package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import kotlin.coroutines.CoroutineContext

class ElectionsViewModelFactory(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider,
    private val coroutineContext: CoroutineContext
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(politicalPreparednessProvider, coroutineContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

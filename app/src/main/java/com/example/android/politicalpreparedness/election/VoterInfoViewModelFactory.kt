package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import kotlin.coroutines.CoroutineContext

class VoterInfoViewModelFactory(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(politicalPreparednessProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

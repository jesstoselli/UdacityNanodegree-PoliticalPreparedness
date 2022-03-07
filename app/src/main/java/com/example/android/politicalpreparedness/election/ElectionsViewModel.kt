package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider
) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfoFragment = MutableLiveData<Election>()
    val navigateToVoterInfoFragment: LiveData<Election>
        get() = _navigateToVoterInfoFragment

    init {
        Log.d(TAG, "Loaded properly")
        getUpcomingElectionsFromAPI()
        getFollowedElectionsFromDatabase()
    }

    fun navigateToVoterInfoFragment(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    fun returnFromVoterInfoFragment() {
        _navigateToVoterInfoFragment.value = null
    }

    fun refreshElectionData() {
        getFollowedElectionsFromDatabase()
    }

    private fun getUpcomingElectionsFromAPI() {
        viewModelScope.launch {
            val electionsList = politicalPreparednessProvider.getElectionsFromAPI()
            _upcomingElections.value = electionsList
        }
    }

    private fun getFollowedElectionsFromDatabase() {
        viewModelScope.launch {
            val electionsList = politicalPreparednessProvider.getFollowedElectionsFromDatabase()
            _savedElections.value = electionsList
        }
    }

    companion object {
        const val TAG = "ElectionsViewModel"
    }
}

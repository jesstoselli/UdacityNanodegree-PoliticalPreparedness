package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.utils.ApiStatus
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider
) : ViewModel() {

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

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
        _apiStatus.value = ApiStatus.LOADING
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
//        _apiStatus.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                val electionsList = politicalPreparednessProvider.getElectionsFromAPI()
                _apiStatus.value = ApiStatus.DONE
                _upcomingElections.value = electionsList

            } catch (e: Exception) {
                val message =
                    if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                Log.e(TAG, message)
                _apiStatus.value = ApiStatus.ERROR
                _upcomingElections.value = emptyList()
            }
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

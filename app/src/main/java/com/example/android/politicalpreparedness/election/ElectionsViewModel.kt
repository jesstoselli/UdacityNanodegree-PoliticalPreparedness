package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ElectionsViewModel(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider,
    override val coroutineContext: CoroutineContext
) : ViewModel(), CoroutineScope {

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
        getUpcomingElectionsFromAPI()
        getFollowedElectionsFromDatabase()
    }

    fun navigateToVoterInfoFragment(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    fun returnFromVoterInfoFragment() {
        _navigateToVoterInfoFragment.value = null
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
}

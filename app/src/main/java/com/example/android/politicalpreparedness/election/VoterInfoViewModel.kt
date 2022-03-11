package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.AdministrationBody
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.utils.ApiStatus
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider,
) : ViewModel() {

    //Add live data to hold voter info
    private val _chosenElection = MutableLiveData<Election>()
    val chosenElection: LiveData<Election>
        get() = _chosenElection

    private val _administrationBody = MutableLiveData<AdministrationBody>()
    val administrationBody: LiveData<AdministrationBody>
        get() = _administrationBody

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private val _isFollowed = MutableLiveData<Boolean>()
    val isFollowed: LiveData<Boolean>
        get() = _isFollowed

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _basicInfo = MutableLiveData<BasicInfo>()
    val basicInfo: LiveData<BasicInfo>
        get() = _basicInfo

    init {
        _apiStatus.value = ApiStatus.LOADING
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    fun retrieveVoterInfo(electionId: Int, division: Division) {

        viewModelScope.launch {
            try {
                val isElectionAlreadyFollowed = politicalPreparednessProvider.getElectionById(electionId)
                _isFollowed.value = isElectionAlreadyFollowed != null

                if (isElectionAlreadyFollowed != null) {
                    _basicInfo.value = BasicInfo(
                        name = isElectionAlreadyFollowed.name,
                        date = isElectionAlreadyFollowed.electionDay.toLocaleString()
                    )
                }

                val voterAddress = "${division.state}, ${division.country}"

                val retrievedVoterInfo = if (division.state.isEmpty()) {
                    politicalPreparednessProvider.getVoterInfo(electionId, division.country)
                } else {
                    politicalPreparednessProvider.getVoterInfo(electionId, voterAddress)
                }

                _chosenElection.value = retrievedVoterInfo.election
                Log.d(TAG, chosenElection.value.toString())
                _administrationBody.value = retrievedVoterInfo.state?.first()?.electionAdministrationBody

                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                val message =
                    if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                Log.e(ElectionsViewModel.TAG, message)
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }

    // Called from the xml with databinding
    fun followUnfollowElection() {
        viewModelScope.launch {
            _chosenElection.value?.let {
                if (_isFollowed.value == true) {
                    politicalPreparednessProvider.unfollowElection(it.id.toInt())
                    _isFollowed.value = false
                } else {
                    politicalPreparednessProvider.followElection(it)
                    _isFollowed.value = true
                }
            }
        }
    }

    fun openLinkOnBrowser(url: String) {
        _url.postValue(url)
    }

    companion object {
        const val TAG = "VoterInfoViewModel"
    }
}

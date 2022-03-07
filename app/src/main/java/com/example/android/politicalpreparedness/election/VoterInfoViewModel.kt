package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.AdministrationBody
import com.example.android.politicalpreparedness.data.network.models.Election
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

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status


    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

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
        _url.value = url
    }

//    fun navigateToUrlCompleted() {
//        _url.value = null
//    }

}

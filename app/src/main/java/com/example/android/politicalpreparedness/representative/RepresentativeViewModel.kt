package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.ApiStatus
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val politicalPreparednessProvider: PoliticalPreparednessProvider
) : ViewModel() {

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _representativeAddress = MutableLiveData<Address>()
    val representativeAddress: LiveData<Address>
        get() = _representativeAddress

    init {
        _representativeAddress.value = Address("", null, "", "", "")
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    fun getRepresentativesFromEnteredAddress(enteredAddress: Address?) {
        _apiStatus.value = ApiStatus.LOADING

        viewModelScope.launch {
            _representatives.value = arrayListOf()
            if (enteredAddress != null) {
                try {
                    _representativeAddress.value = enteredAddress
                    val (offices, officials) = politicalPreparednessProvider.getRepresentativesList(enteredAddress.toFormattedString())
                    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
                    _apiStatus.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _apiStatus.value = ApiStatus.ERROR
                }
            }
        }
    }

    //TODO: Create function get address from geo location
//    fun getRepresentativesFromLocation() {
//
//    }
}

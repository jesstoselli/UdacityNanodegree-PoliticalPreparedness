package com.example.android.politicalpreparedness.data.network.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

data class Address (
    var line1: String,
    var line2: String? = null,
    var city: String,
    var state: String,
    var zip: String
) : BaseObservable() {

    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }

//    @Bindable
//    var address = Address("", "", "", "", "")
//        set(value) {
//            if (value != field) {
//                field = value
//                notifyPropertyChanged(BR.address)
//            }
//        }
}

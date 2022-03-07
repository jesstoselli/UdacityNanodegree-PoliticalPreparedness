package com.example.android.politicalpreparedness

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineContextProvider {
    val main: CoroutineDispatcher
        get() = Dispatchers.Main
    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    class Default : CoroutineContextProvider
}

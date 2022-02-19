package com.example.android.politicalpreparedness.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Election

class ElectionRepository(private val database: ElectionDatabase) {

    // Get list of elections
    suspend fun getElections(): List<Election> {
        return database.electionDao.getElections()
    }

    // Get election by given id
    suspend fun getElectionById(id: Int): Election? {
        return database.electionDao.getElectionById(id)
    }

    // Insert a followed election in db
    suspend fun followElection(election: Election) {
        database.electionDao.saveElection(election)
    }

    // Remove election from db
    suspend fun unfollowElection(id: Int) {
        return database.electionDao.deleteSingleElection(id)
    }

    // Clear elections from db
    suspend fun unfollowAllElections() {
        return database.electionDao.deleteAll()
    }

//    // Refresh election data
//    suspend fun refreshElectionData() {
//        try {
//            val client = CivicsApi.retrofitService
//            val electionsListResponse = client.getElections().elections
//            electionsListResponse.forEach { election ->
//                database.electionDao.saveElection(election)
//            }
//        } catch (e: Exception) {
//            Log.d(TAG, e.message.toString())
//        }
//    }
//
//    companion object {
//        const val TAG = "ElectionRepository"
//    }

}

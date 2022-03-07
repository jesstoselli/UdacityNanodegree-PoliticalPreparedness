package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.utils.wrapEspressoIdlingResource

class ElectionRepository(private val electionDao: ElectionDao) {

    // Get list of elections
    suspend fun getElections(): List<Election> {
        wrapEspressoIdlingResource {
            return electionDao.getElections()
        }
    }

    // Get election by given id
    suspend fun getElectionById(id: Int): Election? {
        wrapEspressoIdlingResource {
            return electionDao.getElectionById(id)
        }
    }

    // Insert a followed election in db
    suspend fun followElection(election: Election) {
        wrapEspressoIdlingResource {
            electionDao.saveElection(election)
        }
    }

    // Remove election from db
    suspend fun unfollowElection(id: Int) {
        wrapEspressoIdlingResource {
            return electionDao.deleteSingleElection(id)
        }
    }

    // Clear elections from db
    suspend fun unfollowAllElections() {
        wrapEspressoIdlingResource {
            return electionDao.deleteAll()
        }
    }

//    // Refresh election data
//    suspend fun refreshElectionData() {
//        try {
//            val client = CivicsApi.retrofitService
//            val electionsListResponse = client.getElections().elections
//            electionsListResponse.forEach { election ->
//                electionDao.saveElection(election)
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

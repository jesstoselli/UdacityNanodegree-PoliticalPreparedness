package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.dataadapters.toDomainModel
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfo

class PoliticalPreparednessProvider(
    private val electionRepository: ElectionRepository,
    private val civicsApiService: CivicsApiService
) {

    // Elections
    suspend fun getFollowedElectionsFromDatabase(): List<Election> {
        return electionRepository.getElections()
    }

    suspend fun getElectionsFromAPI(): List<Election> {
        val electionsList = civicsApiService.getElections()
        return electionsList.toDomainModel()
    }

    suspend fun getElectionById(id: Int): Election? {
        return electionRepository.getElectionById(id)
    }

    suspend fun followElection(election: Election) {
        electionRepository.followElection(election)
    }

    suspend fun unfollowElection(id: Int) {
        electionRepository.unfollowElection(id)
    }

    suspend fun unfollowAllElections() {
        electionRepository.unfollowAllElections()
    }

    // Voter Info
    suspend fun getVoterInfo(electionId: Int, address: String): VoterInfo {
        val voterInfoResponse = civicsApiService.getVoterInfo(electionId, address)
        return voterInfoResponse.toDomainModel()
    }

    // TODO: Check if this is the correct model to return
    // Representatives
    suspend fun getRepresentativesList(address: String): RepresentativeResponse {
        return civicsApiService.getRepresentativesInfoByAddress(address)
    }

}

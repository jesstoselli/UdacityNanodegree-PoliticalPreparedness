package com.example.android.politicalpreparedness.data.network.dataadapters

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.ElectionResponse
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfo
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.Representative


fun ElectionResponse.toDomainModel(): List<Election> {
    return this.elections.map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division
        )
    }
}

fun VoterInfoResponse.toDomainModel(): VoterInfo {
    return VoterInfo(
        election = this.election,
        state = this.state,
        electionElectionOfficials = this.electionElectionOfficials
    )
}

//fun RepresentativeResponse.toDomainModel(): Representative {
//    return Representative(
//        official = this.officials
//    )
//}

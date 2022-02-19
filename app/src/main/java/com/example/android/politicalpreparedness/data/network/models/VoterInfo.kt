package com.example.android.politicalpreparedness.data.network.models

data class VoterInfo(
    val election: Election,
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
    )

package com.example.android.politicalpreparedness.network.dataadapters

import com.example.android.politicalpreparedness.network.dtos.DivisionData
import com.example.android.politicalpreparedness.network.dtos.ElectionData
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse

fun DivisionData.toDomainModel(): Division = Division(
    id = this.id,
    country = this.country,
    state = this.state
)

fun DivisionData.toDatabaseModel(): Division = Division(
    id = this.id,
    country = this.country,
    state = this.state
)

fun List<ElectionData>.toDomainModel(): List<Election> {
    return this.map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDate,
            division = it.division.toDomainModel()
        )
    }
}

//fun List<ElectionResponse>.toDatabaseModel(): Array<Election> {
//    return this.map {
//        Election(
//            id = it.,
//            name = it.name,
//            electionDay = it.electionDay,
//            division = it.division.asDatabaseModal()
//        )
//    }.toTypedArray()
//}

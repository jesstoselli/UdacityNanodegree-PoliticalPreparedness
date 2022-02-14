package com.example.android.politicalpreparedness.network.dtos

import com.squareup.moshi.Json
import java.util.*

data class ElectionDataResponse(
    val kind: String,
    val elections: String
)

data class ElectionData(
    val id: Int,
    val name: String,
    val electionDate: Date,
    @Json(name = "ocdDivisionId") val division: DivisionData
)

data class DivisionData(
    val id: String,
    val country: String,
    val state: String
)

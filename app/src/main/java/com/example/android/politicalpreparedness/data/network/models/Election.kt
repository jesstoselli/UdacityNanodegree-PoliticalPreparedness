package com.example.android.politicalpreparedness.data.network.models

import androidx.room.*
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
@JsonClass(generateAdapter = true)
data class Election(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Json(name="ocdDivisionId") val division: String
)

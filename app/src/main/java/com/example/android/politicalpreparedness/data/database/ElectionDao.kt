package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    //Insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    //Select all election query
    @Query("SELECT * FROM election_table")
    suspend fun getElections(): List<Election>

    //Select single election query
    @Query("SELECT * FROM election_table where id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?

    //Delete single entry query
    @Query("DELETE FROM election_table where id = :electionId")
    suspend fun deleteSingleElection(electionId: Int)

    //Clear query
    @Query("DELETE FROM election_table")
    suspend fun deleteAll()

}

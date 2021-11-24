package com.example.websitetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * This Data Access Object allows us to isolate the persistence layer (relational database).
 *
 * It describes all the operations allowed on the "search" table throughout our application.
 */

@Dao
interface SearchDao {
    @Query("SELECT * FROM search")
    suspend fun getAll(): List<Search>

    @Query("SELECT * FROM search WHERE ts IN (SELECT MAX(search.ts) FROM search)")
    fun getLast(): LiveData<Search?>

    @Query("SELECT * FROM search WHERE url = :address")
    suspend fun get(address: String): Search?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: Search)

    @Delete
    suspend fun delete(search: Search)

    @Query("DELETE FROM search")
    suspend fun deleteAll()
}

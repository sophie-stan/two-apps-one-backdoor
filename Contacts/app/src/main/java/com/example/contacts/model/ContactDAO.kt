package com.example.contacts.model

import android.database.Cursor
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): Cursor

    @Query("SELECT * FROM contact WHERE id LIKE :contactId")
    fun findById(contactId: String): Cursor

    @Query("SELECT * FROM contact WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Contact

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact): Long

    @Insert
    fun insertAll(vararg contacts: Contact)

    @Update
    fun update(contact: Contact): Int

    @Delete
    fun delete(contact: Contact)

    @Query(value = "DELETE FROM contact WHERE id LIKE :contactId")
    fun deleteById(contactId: String) : Int
}

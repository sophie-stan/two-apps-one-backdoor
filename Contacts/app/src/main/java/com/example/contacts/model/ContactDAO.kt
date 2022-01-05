package com.example.contacts.model

import android.database.Cursor
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): Cursor

    @Query("SELECT DISTINCT name FROM contact ORDER BY name")
    suspend fun getAllNameList(): List<String>

    @Query("SELECT * FROM contact WHERE contact_id LIKE :contactId")
    fun getByContactId(contactId: String): Cursor

    @Query("SELECT DISTINCT numbers FROM contact WHERE name LIKE :name")
    suspend fun getNumbersByName(name: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact): Long

    @Insert
    fun insertAll(vararg contacts: Contact)

    @Update
    fun update(contact: Contact): Int

    @Delete
    fun delete(contact: Contact)

    @Query(value = "DELETE FROM contact WHERE contact_id LIKE :contactId")
    fun deleteByContactId(contactId: String): Int

    @Query("DELETE FROM contact")
    suspend fun clear()

}

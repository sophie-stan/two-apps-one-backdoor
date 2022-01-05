package com.example.contacts.model

interface ContactDatabaseHelper {

    suspend fun getContactsName(): List<String>

    suspend fun getContactsByName(contactName: String): List<String>

    suspend fun insert(contact: Contact): Long

    suspend fun clear()

}
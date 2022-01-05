package com.example.contacts.model

class ContactDatabaseHelperImpl(private val appDatabase: ContactDatabase) : ContactDatabaseHelper {

    override suspend fun getContactsName(): List<String> = appDatabase.contactDao.getAllNameList()

    override suspend fun getContactsByName(contactName: String): List<String> =
        appDatabase.contactDao.getNumbersByName(contactName)

    override suspend fun insert(contact: Contact) = appDatabase.contactDao.insert(contact)

    override suspend fun clear() = appDatabase.contactDao.clear()

}
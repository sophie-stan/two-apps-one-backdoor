package com.example.contacts

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.provider.ContactsContract.Data.*
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
    }

    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        )
            showPermissionReasonAndRequest(
                "Grant permission",
                "To work properly, the application needs to access your contacts.\nPlease grant Contacts permission."
            )
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun Activity.showPermissionReasonAndRequest(
        title: String,
        message: String,
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener(function = positiveButtonClick)
            )
            .show()
    }

    fun addContact(view: View) {
        val contactName: EditText = findViewById(R.id.new_contact_name)
        val contactNumber: EditText = findViewById(R.id.new_contact_number)
        val name: String = contactName.text.toString()
        val phone = contactNumber.text.toString()
        val addContactsUri: Uri = CONTENT_URI

        // Add an empty contact and get the generated id.
        val rowContactId: Long = getRawContactId()
        // Add contact name data.
        insertContactDisplayName(addContactsUri, rowContactId, name)
        // Add contact phone data.
        insertContactPhoneNumber(addContactsUri, rowContactId, phone)

        Toast.makeText(
            applicationContext,
            "$name was added successfully!",
            Toast.LENGTH_LONG
        ).show()

        startActivity(Intent(this, MainActivity::class.java))
    }


    // This method will only insert an empty data to RawContacts.CONTENT_URI
    // The purpose is to get a system generated raw contact id.
    private fun getRawContactId(): Long {
        val contentValues = ContentValues()
        val rawContactUri: Uri = contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            contentValues
        ) ?: return -1
        // Get the newly created contact raw id.
        return ContentUris.parseId(rawContactUri)
    }

    private fun insertContactDisplayName(
        addContactsUri: Uri,
        rawContactId: Long,
        displayName: String
    ) {
        // Create a ContentValues object.
        val contentValues = ContentValues()
        contentValues.put(RAW_CONTACT_ID, rawContactId)
        contentValues.put(
            MIMETYPE,
            StructuredName.CONTENT_ITEM_TYPE
        )
        // Put name
        contentValues.put(StructuredName.GIVEN_NAME, displayName)
        contentResolver.insert(addContactsUri, contentValues)
    }

    private fun insertContactPhoneNumber(
        addContactsUri: Uri,
        rawContactId: Long,
        phoneNumber: String
    ) {
        // Create a ContentValues object.
        val contentValues = ContentValues()
        contentValues.put(RAW_CONTACT_ID, rawContactId)
        contentValues.put(
            MIMETYPE,
            Phone.CONTENT_ITEM_TYPE
        )
        // Put phone number value.
        contentValues.put(Phone.NUMBER, phoneNumber)
        // Insert new contact data into phone contact list.
        contentResolver.insert(addContactsUri, contentValues)
    }
}
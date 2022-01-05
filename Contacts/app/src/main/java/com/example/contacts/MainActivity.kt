package com.example.contacts

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.model.Contact
import com.example.contacts.model.ContactDatabase
import com.example.contacts.model.ContactDatabaseHelperImpl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_READ_CONTACTS = 79
        lateinit var contactHelper: ContactDatabaseHelperImpl
    }

    private var data = ArrayList<String>()

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contactHelper = ContactDatabaseHelperImpl(ContactDatabase.getDatabase(applicationContext))
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            contactHelper.clear()

            val recyclerview = findViewById<RecyclerView>(R.id.contact_list)

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    READ_CONTACTS
                ) == PERMISSION_GRANTED
            ) {
                loadContactFromProvider()
            } else {
                showPermissionReasonAndRequest(
                    "Grant permission",
                    "To work properly, the application needs to access your contacts.\nPlease grant Contacts permission."
                )
            }
            data = contactHelper.getContactsName() as ArrayList<String>
            recyclerview.adapter = ContactAdapter(data)
        }
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

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                loadContactFromProvider()
            }
        }
    }

    @SuppressLint("Range")
    private fun loadContactFromProvider() {
        val contentResolver = contentResolver
        val cursor = contentResolver.query(CONTENT_URI, null, null, null, DISPLAY_NAME)

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {

                val id = cursor.getString(cursor.getColumnIndex(CONTACT_ID))
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER))

                // Retrieve the phone numbers from provider.
                if (hasPhoneNumber > 0) {
                    val phoneCursor = contentResolver.query(
                        CONTENT_URI,
                        arrayOf(NUMBER),
                        "$CONTACT_ID = ?",
                        arrayOf(id),
                        null
                    )
                    phoneCursor!!.moveToFirst()
                    while (!phoneCursor.isAfterLast) {
                        val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER))
                            .removeWhitespace()
                        val contact = Contact(id, name, phoneNumber)
                        lifecycleScope.launch {
                            contactHelper.insert(contact)
                        }
                        phoneCursor.moveToNext()
                    }
                    phoneCursor.close()
                }
            }
            cursor.close()
        }
    }

    fun onShowContact(view: android.view.View) {
        val contactName = (view as TextView).text.toString()

        startActivity(
            Intent(this, ContactDetailActivity::class.java)
                .putExtra("contact", contactName)
        )
    }

    fun newContact(view: android.view.View) {
        startActivity(
            Intent(this, AddContactActivity::class.java)
        )
    }
}
package com.example.contacts

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.model.Contact
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private val REQUEST_READ_CONTACTS = 79
    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // ArrayList of class Contact
    var data = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        data.clear()
        // Getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.contact_list)

        if (ActivityCompat.checkSelfPermission(this, READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            loadContactFromProvider()
        } else {
            showPermissionReasonAndRequest(
                "Notice",
                "We noticed READ CONTACTS permission is disabled. " +
                        "We will take you to the Application settings, " +
                        "you can re-enable the permission there"
            )
        }

        // This will pass the ArrayList to our Adapter
        val adapter = ContactAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    private fun Activity.showPermissionReasonAndRequest(
        title: String,
        message: String,
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        builder.show()
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    loadContactFromProvider()
                }
                return
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
                var phoneNumbers = ArrayList<String>()

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
                            .replace(" ", "")
                        phoneNumbers.add(phoneNumber)
                        phoneCursor.moveToNext()
                    }
                    phoneCursor.close()
                }
                var contact = Contact(name, phoneNumbers)
                if (!data.contains(contact)) {
                    data.add(contact)
                }
            }
            cursor.close()
        }
    }

    private fun findContact(text: String): Contact? {
        for (contact in data){
            if (contact.name == text){
                return contact
            }
        }
        return null
    }

    fun showContact(view: android.view.View) {
        var selected: TextView = view as TextView
        val contact = findContact(selected.text as String)
        val myIntent = Intent(this, ContactDetail::class.java)
        myIntent.putExtra("contact", contact as Serializable)
        startActivity(myIntent)
    }


}
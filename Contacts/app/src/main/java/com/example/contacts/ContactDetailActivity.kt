package com.example.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ContactDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        val contactName = intent.getSerializableExtra("contact") as String
        val contactNameView = findViewById<TextView>(R.id.contact_name_title)
        contactNameView.text = contactName

        lifecycleScope.launch {
            val contactNumber =
                MainActivity.contactHelper.getContactsByName(contactName) as ArrayList<String>

            val arrayAdapter =
                ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_selectable_list_item,
                    contactNumber
                )

            findViewById<ListView>(R.id.contact_numbers).adapter = arrayAdapter
        }

    }
}



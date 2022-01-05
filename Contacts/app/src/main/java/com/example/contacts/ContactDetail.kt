package com.example.contacts

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ContactDetail : AppCompatActivity() {

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



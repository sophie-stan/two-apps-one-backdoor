package com.example.contacts

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.contacts.model.Contact

class ContactDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        val contact = intent.getSerializableExtra("contact") as? Contact
        val contactName = findViewById<TextView>(R.id.contact_name_title)
        if (contact != null) {
            contactName.text = contact.name
            val arrayAdapter =
                ArrayAdapter(this, android.R.layout.simple_selectable_list_item, contact.numbers)
            findViewById<ListView>(R.id.contact_numbers).adapter = arrayAdapter
        }
    }

}
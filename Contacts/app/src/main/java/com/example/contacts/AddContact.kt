package com.example.contacts

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
    }

    fun addContact(view: View) {
        val contactName: EditText = findViewById(R.id.new_contact_name)
        val contactNumber: EditText = findViewById(R.id.new_contact_number)
        val name: String = contactName.text.toString()
        val phone = contactNumber.text.toString()
    }
}
package com.example.contacts.model

import java.io.Serializable

data class Contact(
    var name: String,
    var numbers: ArrayList<String>,
) : Serializable

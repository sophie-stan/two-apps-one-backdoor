package com.example.contacts.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey val id: String,
    val name: String,
    val numbers: String,
) : Serializable

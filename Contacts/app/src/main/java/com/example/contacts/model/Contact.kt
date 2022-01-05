package com.example.contacts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contact")
data class Contact(
    @ColumnInfo(name = "contact_id") val contactId: String,
    val name: String,
    val numbers: String,
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
}

package com.example.websitetracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "search")
data class Search(
    @PrimaryKey
    val url: String,

    @ColumnInfo(name = "rating")
    val rating: Float? = null,

    @ColumnInfo(name = "ts")
    val ts: Long = System.currentTimeMillis(),
) {

    override fun toString(): String {
        val readableDate = SimpleDateFormat("MM/dd/yyyy, hh:mm a", Locale.FRANCE).format(Date(ts))
        return "$url\n" +
                "Last visit: $readableDate\n" +
                "Given rating: ${NumberFormat.getInstance().format(rating)}/5"
    }
}

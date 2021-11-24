package com.example.websitetracker.rating

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.websitetracker.database.SearchDao

/**
 * Boiler plate code for a ViewModel Factory.
 */
class RatingViewModelFactory(
    private val application: Application,
    private val dataSource: SearchDao,
    private val url: String
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingViewModel::class.java)) {
            return RatingViewModel(application, dataSource, url) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

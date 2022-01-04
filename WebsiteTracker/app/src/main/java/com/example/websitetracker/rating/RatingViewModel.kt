package com.example.websitetracker.rating

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.websitetracker.R
import com.example.websitetracker.database.Search
import com.example.websitetracker.database.SearchDao
import kotlinx.coroutines.launch

class RatingViewModel(
    application: Application,
    private val searchDao: SearchDao,
    private val url: String
) : ViewModel() {

    private val _rateMessage =
        MutableLiveData(application.resources.getString(R.string.rate_message, url))
    val ratingMessage: LiveData<String> = _rateMessage

    private var currentRating: Float = 0f

    private var _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean> = _navigateToSearch

    fun onRated(rating: Float) {
        Log.i("RatingViewModel", "Website rated at $rating")
        currentRating = rating
    }

    fun onValidate() {
        viewModelScope.launch {
            searchDao.insert(Search(url, currentRating))
        }
        _navigateToSearch.value = true
    }

    fun onRatedComplete() {
        _navigateToSearch.value = false
    }
}

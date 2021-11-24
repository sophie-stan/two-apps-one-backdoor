package com.example.websitetracker.search

import android.util.Log
import androidx.lifecycle.*
import com.example.websitetracker.database.Search
import com.example.websitetracker.database.SearchDao
import kotlinx.coroutines.launch

class SearchViewModel(private val searchDao: SearchDao) : ViewModel() {

    private var _urlToSearch = MutableLiveData<String>()
    val urlToSearch: LiveData<String> = _urlToSearch

    private var _searchRatingToShow = MutableLiveData<Search>()
    val searchRatingToShow: LiveData<Search> = _searchRatingToShow

    val lastSearch: LiveData<Search?> = searchDao.getLast() // Listen directly from database.

    private var _liveInput = MutableLiveData<String>()
    val liveInput: LiveData<String> = _liveInput

    private var _showSuccessfulClean = MutableLiveData<Boolean>()
    val showSuccessfulClean: LiveData<Boolean> = _showSuccessfulClean

    fun onSearch() {
        Log.i("SearchViewModel", "User entered ${_liveInput.value}")

        // Transform user input into a correct url.
        val url = _liveInput.value
            .toString()
            .let { input ->
                if (input.startsWith(SearchFragment.START_URL)) input
                else SearchFragment.START_URL.plus(input)
            }

        // Check if there is a previous rating for this website.
        viewModelScope.launch {
            searchDao.get(url).let {
                if (it != null) {
                    _searchRatingToShow.value = it
                } else {
                    Log.i("SearchViewModel", "First time query for $url")
                    searchDao.insert(Search(url))
                    _urlToSearch.value = url
                }
            }
        }
    }

    fun onCleanHistory() {
        viewModelScope.launch {
            searchDao.deleteAll()
            _showSuccessfulClean.value = true
        }
    }

    fun onTextChanged(text: CharSequence) {
        _liveInput.value = text.toString()
    }

    fun updateSearch(url: String) {
        viewModelScope.launch {
            searchDao.insert(Search(url))
        }
    }
}

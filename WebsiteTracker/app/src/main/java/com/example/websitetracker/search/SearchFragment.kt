package com.example.websitetracker.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.websitetracker.R
import com.example.websitetracker.database.AppDatabase
import com.example.websitetracker.database.Search
import com.example.websitetracker.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        const val START_URL = "https://"
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        val application = requireNotNull(activity).application
        val dataSource = AppDatabase.getInstance(application).searchDao
        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(dataSource))[SearchViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.urlToSearch.observe(viewLifecycleOwner, { startSearch(it) })

        viewModel.searchRatingToShow.observe(viewLifecycleOwner, { showGivenRating(it) })

        viewModel.lastSearch.observe(viewLifecycleOwner, {
            if (it != null && it.rating == null) {
                navigateToRating(it.url)
            }
        })

        viewModel.showSuccessfulClean.observe(viewLifecycleOwner, { if (it) showSuccessfulClean() })

        return binding.root
    }

    private fun showGivenRating(search: Search) {
        Log.i("SearchFragment", "Show last Rating")

         activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Continue?")
                setMessage("$search")
                setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.updateSearch(search.url)
                    startSearch(search.url)
                }
                setNegativeButton(R.string.cancel) { _, _ -> }
            }
            builder.create()
        }?.show()
    }

    private fun startSearch(url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(webIntent)
    }

    private fun navigateToRating(url: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToRatingFragment(url)
        val navController = this.findNavController()
        navController.navigate(action)
    }

    private fun showSuccessfulClean() {
        Toast.makeText(
            activity as Context,
            "Successful clean",
            Toast.LENGTH_SHORT
        ).show()
    }
}

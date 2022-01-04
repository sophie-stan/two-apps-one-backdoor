package com.example.websitetracker.rating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.websitetracker.R
import com.example.websitetracker.database.AppDatabase
import com.example.websitetracker.databinding.FragmentRatingBinding

class RatingFragment : Fragment() {

    private val args: RatingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentRatingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_rating,
            container,
            false
        )

        val application = requireNotNull(activity).application
        val dataSource = AppDatabase.getInstance(application).searchDao
        val viewModel = ViewModelProvider(
            this,
            RatingViewModelFactory(application, dataSource, args.url)
        )[RatingViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.navigateToSearch.observe(viewLifecycleOwner, {
            if (it) {
                Log.d("RatingFragment", "Navigating to SearchFragment")
                findNavController().navigate(RatingFragmentDirections.actionRatingFragmentToSearchFragment())
                viewModel.onRatedComplete()
            }
        })

        return binding.root
    }
}

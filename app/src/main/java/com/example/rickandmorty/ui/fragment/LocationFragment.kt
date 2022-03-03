package com.example.rickandmorty.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.databinding.FragmentEpisodesBinding
import com.example.rickandmorty.databinding.FragmentLocationBinding
import com.example.rickandmorty.ui.adapter.CharacterPagingAdapter
import com.example.rickandmorty.ui.adapter.LocationPagingAdapter
import com.example.rickandmorty.ui.adapter.paging.CommonLoadStateAdapter
import com.example.rickandmorty.ui.viewmodel.CharacterViewModel
import com.example.rickandmorty.ui.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var binding: FragmentLocationBinding
    private val locationAdapter : LocationPagingAdapter = LocationPagingAdapter()
    private val viewModel: LocationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupRequests()
    }

    private fun setupRequests() {
        viewModel.getLocations().observe(this){
            lifecycleScope.launchWhenStarted {
                locationAdapter.submitData(it)
            }
        }
    }

    private fun setupRecycler()  = with(binding.locationRecycler){
        adapter = locationAdapter.withLoadStateFooter(CommonLoadStateAdapter{
            locationAdapter.retry()
            locationAdapter.refresh()
        })
        locationAdapter.addLoadStateListener { loadStates->
            this.isVisible = loadStates.refresh is LoadState.NotLoading
        }
    }

}
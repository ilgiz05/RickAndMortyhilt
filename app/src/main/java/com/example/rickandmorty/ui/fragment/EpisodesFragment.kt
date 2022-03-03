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
import com.example.rickandmorty.ui.adapter.EpisodePagingAdapter
import com.example.rickandmorty.ui.adapter.LocationPagingAdapter
import com.example.rickandmorty.ui.adapter.paging.CommonLoadStateAdapter
import com.example.rickandmorty.ui.viewmodel.EpisodeViewModel
import com.example.rickandmorty.ui.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodesFragment : Fragment(R.layout.fragment_episodes) {

    private lateinit var binding: FragmentEpisodesBinding
    private val episodesAdapter : EpisodePagingAdapter = EpisodePagingAdapter()
    private val viewModel: EpisodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupRequests()
    }

    private fun setupRecycler() {
        viewModel.getEpisodes().observe(this){
            lifecycleScope.launchWhenStarted {
                episodesAdapter.submitData(it)
            }
        }
    }

    private fun setupRequests()  = with(binding.episodesRecycler){
        adapter = episodesAdapter.withLoadStateFooter(CommonLoadStateAdapter{
            episodesAdapter.retry()
            episodesAdapter.refresh()
        })
        episodesAdapter.addLoadStateListener { loadStates->
            this.isVisible = loadStates.refresh is LoadState.NotLoading
        }
    }
}
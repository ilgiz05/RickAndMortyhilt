package com.example.rickandmorty.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.ui.adapter.CharacterPagingAdapter
import com.example.rickandmorty.ui.adapter.paging.CommonLoadStateAdapter
import com.example.rickandmorty.ui.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : Fragment(R.layout.fragment_character) {

    private val viewModel: CharacterViewModel by viewModels()
    private val characterAdapter : CharacterPagingAdapter = CharacterPagingAdapter()
    private lateinit var binding: FragmentCharacterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupRequests()
    }

    private fun setupRequests() {
        viewModel.getCharacters().observe(this@CharacterFragment){
            lifecycleScope.launchWhenStarted {
                characterAdapter.submitData(it)
            }
        }
    }

    private fun setupRecycler() = with(binding.characterRecycler){
        adapter = characterAdapter.withLoadStateFooter(CommonLoadStateAdapter{
            characterAdapter.retry()
            characterAdapter.refresh()
        })
        characterAdapter.addLoadStateListener { loadStates->
            this.isVisible = loadStates.refresh is LoadState.NotLoading
        }
    }
}

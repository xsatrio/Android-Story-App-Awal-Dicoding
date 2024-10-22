package com.dicoding.storyapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.ViewModelFactory
import com.dicoding.storyapp.data.Results
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.ui.adapter.StoriesAdapter
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyAdapter = StoriesAdapter { storyId ->
            // Handle item click, e.g., navigate to detail screen
        }

        viewModel.getAllStories.observe(this) {result ->
            when (result) {
                is Results.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Results.Success -> {
                    binding.progressBar.visibility = View.GONE
                    storyAdapter.submitList(result.data)
                }
                is Results.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Error: ${result.error}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter
        }
    }
}
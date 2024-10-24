package com.dicoding.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.ViewModelFactory
import com.dicoding.storyapp.data.Results
import com.dicoding.storyapp.data.pref.UserPref
import com.dicoding.storyapp.dataStore
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.ui.adapter.StoriesAdapter
import com.dicoding.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.storyapp.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoriesAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                lifecycleScope.launch {
                    val userPref = UserPref.getInstance(dataStore)
                    userPref.clearToken()

                    Snackbar.make(binding.root, "Logged out successfully", Snackbar.LENGTH_SHORT)
                        .show()
                    delay(1000)
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoriesAdapter()

        observeViewModel()

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.getAllStories.observe(this) { result ->
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
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }
}

package com.example.demoproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.data.model.MainRepository
import com.example.data.model.MoviePagerAdapter
import com.example.data.model.RetrofitService
import com.example.demoproject.databinding.ActivityMovieListBinding
import com.example.viewmodels.MovieViewModel
import com.example.viewmodels.MovieViewModelFactory
import kotlinx.coroutines.launch

class MovieListActivity : AppCompatActivity() {
    lateinit var viewModel: MovieViewModel
    private val adapter = MoviePagerAdapter()
    lateinit var binding: ActivityMovieListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(mainRepository)
        ).get(MovieViewModel::class.java)

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
                binding.progressDialog.isVisible = true
            else {
                binding.progressDialog.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }

        lifecycleScope.launch {
            viewModel.getMovieList().observe(this@MovieListActivity) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }
}
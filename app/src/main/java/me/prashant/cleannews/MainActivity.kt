package me.prashant.cleannews

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.prashant.cleannews.databinding.ActivityMainBinding
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.presentation.viewmodel.NewsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defaultAndroid()
        setUpObservers()
        hitApi()
    }

    private fun hitApi() {
        newsViewModel.fetchNews()
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            newsViewModel.state.collect { state ->
                when (state) {
                    is ArticleListState.Loading -> {
                        Log.v("UILayer", "Articles: $state")
                    }
                    is ArticleListState.Success -> {
                        Log.v("UILayer", "Articles: $state")
                    }
                    is ArticleListState.Error -> {
                        Log.v("UILayer", "Articles: $state")
                    }
                }
            }
        }
    }

    private fun defaultAndroid() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

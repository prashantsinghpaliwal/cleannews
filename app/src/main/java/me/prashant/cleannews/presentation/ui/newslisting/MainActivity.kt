package me.prashant.cleannews.presentation.ui.newslisting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.prashant.cleannews.R
import me.prashant.cleannews.databinding.ActivityMainBinding
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.presentation.viewmodel.NewsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val newsViewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    private val scrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!newsViewModel.isLoading &&
                    totalItemCount <= (lastVisibleItem + 2) &&
                    newsViewModel.ifMoreDataPresent()
                ) {
                    hitApi()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defaultAndroid()
        setUpObservers()
        hitApi()
        setUpUI()
    }

    private fun setUpUI() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle home navigation
                    true
                }
                R.id.navigation_search -> {
                    // Handle dashboard navigation
                    true
                }
                R.id.navigation_favorite -> {
                    // Handle notifications navigation
                    true
                }
                else -> false
            }
        }
    }

    private fun hitApi() {
        newsViewModel.fetchNews()
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            newsViewModel.state.collect { state ->
                when (state) {
                    is ArticleListState.Loading -> {
                        binding.progressBar.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE
                    }

                    is ArticleListState.Success -> {
                        Log.v("UILayer", "Articles: $state")
                        newsAdapter.submitList(state.articles)
                        binding.progressBar.visibility = View.GONE
                    }

                    is ArticleListState.Error -> {
                        Log.v("UILayer", "Articles: $state")
                        binding.progressBar.visibility = View.GONE
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

package me.prashant.cleannews.presentation.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.prashant.cleannews.databinding.FragmentSearchNewsListingBinding
import me.prashant.cleannews.presentation.extensions.addTextChangedListenerDebounce
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.presentation.ui.newslisting.NewsAdapter
import me.prashant.cleannews.presentation.viewmodel.SearchNewsViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SearchNewsListingFragment : Fragment() {
    private var _binding: FragmentSearchNewsListingBinding? = null
    private val binding get() = _binding!!

    private val newsViewModel: SearchNewsViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter
    private var searchQuery = ""

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
                    hitApi(searchQuery, true)
                }
            }
        }

    private fun hitApi(
        searchQuery: String,
        pagination: Boolean = false,
    ) {
        this.searchQuery = searchQuery
        newsViewModel.searchNews(queryString = searchQuery, pagination = pagination)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchNewsListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setUpUI()
    }

    private fun setUpUI() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }

        binding.searchEditText.addTextChangedListenerDebounce(scope = lifecycleScope) { searchText ->
            hitApi(searchText)
        }
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
                        newsAdapter.submitList(state.articles)
                        binding.progressBar.visibility = View.GONE
                    }

                    is ArticleListState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

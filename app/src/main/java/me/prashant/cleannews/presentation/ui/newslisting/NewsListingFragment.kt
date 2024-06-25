package me.prashant.cleannews.presentation.ui.newslisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.prashant.cleannews.R
import me.prashant.cleannews.databinding.FragmentHomeNewsListingBinding
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.presentation.viewmodel.NewsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NewsListingFragment : Fragment() {
    private var _binding: FragmentHomeNewsListingBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeNewsListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        hitApi()
        setUpUI()
    }

    private fun setUpUI() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }

        binding.searchAction.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
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

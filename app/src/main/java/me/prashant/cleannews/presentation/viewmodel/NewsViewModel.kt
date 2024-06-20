package me.prashant.cleannews.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.prashant.cleannews.domain.model.ArticleDomainModel
import me.prashant.cleannews.domain.model.NewsDomainModel
import me.prashant.cleannews.domain.usecase.GetNewsUseCase
import me.prashant.cleannews.presentation.mapper.NewsDomainToUiMapper
import me.prashant.cleannews.presentation.model.NewsUIModel
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.util.core.Resource
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
    @Inject
    constructor(
        private val getNewsUseCase: GetNewsUseCase,
        private val newsMapper: NewsDomainToUiMapper,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ArticleListState>(ArticleListState.Loading(false))
        val state: StateFlow<ArticleListState> get() = _state

        var isLoading = false
        private var currentPage = 1

        private var articleListDomainModel = listOf<ArticleDomainModel>()
        var uiModel: NewsUIModel? = null

        fun ifMoreDataPresent(): Boolean = (uiModel?.totalResults?: 0) > articleListDomainModel.size

        fun fetchNews() {
            viewModelScope.launch {
                if (isLoading) return@launch
                isLoading = true
                getNewsUseCase(currentPage)
                    .catch { e ->
                        _state.value = ArticleListState.Error(e.message ?: "An error occurred")
                    }.collect { result ->
                        when (result) {
                            is Resource.Loading -> _state.value = ArticleListState.Loading(result.isLoading)
                            is Resource.Success -> {
                                articleListDomainModel =
                                    if (currentPage == 1) {
                                        result.data.articles
                                    } else {
                                        articleListDomainModel + result.data.articles
                                    }

                                val domainModel =
                                    NewsDomainModel(
                                        result.data.status,
                                        result.data.totalResults,
                                        articleListDomainModel,
                                    )

                                uiModel = newsMapper.convert(domainModel)
                                uiModel?.let {
                                    currentPage++
                                    isLoading = false
                                    _state.value =
                                        ArticleListState.Success(it.totalResults, it.articles)
                                }
                            }

                            is Resource.Error ->
                                _state.value =
                                    ArticleListState.Error(
                                        result.exception.message ?: "An error occurred",
                                    )
                        }
                    }
            }
        }
    }

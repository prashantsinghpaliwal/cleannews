package me.prashant.cleannews.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.prashant.cleannews.domain.usecase.GetNewsUseCase
import me.prashant.cleannews.presentation.mapper.ArticleDomainToUiMapper
import me.prashant.cleannews.presentation.states.ArticleListState
import me.prashant.cleannews.util.core.Resource
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
    @Inject
    constructor(
        private val getNewsUseCase: GetNewsUseCase,
        private val articleMapper: ArticleDomainToUiMapper,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ArticleListState>(ArticleListState.Loading)
        val state: StateFlow<ArticleListState> get() = _state

        fun fetchNews() {
            viewModelScope.launch {
                getNewsUseCase()
                    .catch { e ->
                        _state.value = ArticleListState.Error(e.message ?: "An error occurred")
                    }.collect { result ->
                        when (result) {
                            is Resource.Loading -> _state.value = ArticleListState.Loading
                            is Resource.Success -> {
                                val uiModels = result.data.map { articleMapper.convert(it) }
                                _state.value = ArticleListState.Success(uiModels)
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

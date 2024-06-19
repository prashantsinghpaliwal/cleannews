package me.prashant.cleannews.presentation.states

import me.prashant.cleannews.presentation.model.ArticleUiModel

sealed class ArticleListState {
    data class Loading(val isLoading: Boolean) : ArticleListState()

    data class Success(
        val totalResults: Int,
        val articles: List<ArticleUiModel>,
    ) : ArticleListState()

    data class Error(
        val message: String,
    ) : ArticleListState()
}

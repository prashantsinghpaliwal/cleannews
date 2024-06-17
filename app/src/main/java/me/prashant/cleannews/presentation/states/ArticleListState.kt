package me.prashant.cleannews.presentation.states

import me.prashant.cleannews.presentation.model.ArticleUiModel

sealed class ArticleListState {
    data object Loading : ArticleListState()

    data class Success(
        val articles: List<ArticleUiModel>,
    ) : ArticleListState()

    data class Error(
        val message: String,
    ) : ArticleListState()
}

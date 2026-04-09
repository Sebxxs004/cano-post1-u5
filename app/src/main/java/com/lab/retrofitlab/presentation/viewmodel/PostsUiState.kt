package com.lab.retrofitlab.presentation.viewmodel

import com.lab.retrofitlab.domain.model.Post

sealed class PostsUiState {
    data object Loading : PostsUiState()
    data object Empty : PostsUiState()
    data class Success(val posts: List<Post>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}


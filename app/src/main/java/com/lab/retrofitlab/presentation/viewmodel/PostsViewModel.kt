package com.lab.retrofitlab.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.retrofitlab.di.NetworkModule
import com.lab.retrofitlab.domain.error.toAppError
import com.lab.retrofitlab.domain.error.toMessage
import com.lab.retrofitlab.domain.model.Post
import data.repository.PostRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {
    private val repo = PostRepositoryImpl(NetworkModule.postApi)

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private val allPosts = mutableListOf<Post>()

    init {
        loadPosts()
    }

    fun loadPosts() {
        currentPage = 1
        allPosts.clear()

        viewModelScope.launch {
            _uiState.value = PostsUiState.Loading
            repo.getPosts(currentPage)
                .onSuccess { posts ->
                    allPosts.addAll(posts)
                    _uiState.value = if (allPosts.isEmpty()) {
                        PostsUiState.Empty
                    } else {
                        PostsUiState.Success(allPosts.toList())
                    }
                }
                .onFailure { error ->
                    _uiState.value = PostsUiState.Error(error.toAppError().toMessage())
                }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val nextPage = currentPage + 1
            repo.getPosts(nextPage)
                .onSuccess { newPosts ->
                    if (newPosts.isNotEmpty()) {
                        currentPage = nextPage
                        allPosts.addAll(newPosts)
                    }
                    _uiState.value = if (allPosts.isEmpty()) {
                        PostsUiState.Empty
                    } else {
                        PostsUiState.Success(allPosts.toList())
                    }
                }
                .onFailure { error ->
                    _uiState.value = PostsUiState.Error(error.toAppError().toMessage())
                }
        }
    }
}

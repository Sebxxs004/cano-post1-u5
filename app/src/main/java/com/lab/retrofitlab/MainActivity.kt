package com.lab.retrofitlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.lab.retrofitlab.presentation.ui.PostsScreen
import com.lab.retrofitlab.presentation.viewmodel.PostsViewModel
import com.lab.retrofitlab.ui.theme.RetrofitLabTheme

class MainActivity : ComponentActivity() {
    private val postsViewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitLabTheme {
                PostsScreen(viewModel = postsViewModel)
            }
        }
    }
}
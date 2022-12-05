package com.example.memeplay.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memeplay.model.Meme
import com.example.memeplay.repository.MemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class ScreenViewModel @Inject constructor(private val memeRepository: MemeRepository)
    : ViewModel() {

    private val meme: MutableState<Meme> = mutableStateOf(
        Meme(
            author = "",
            nsfw = false,
            postLink = "",
            preview = listOf(""),
            spoiler = false,
            subreddit = "",
            title = "",
            ups = 0,
            url = ""
        )
    )

        init {
            getMeme()
        }

    fun getMeme() : Meme{
        viewModelScope.launch {
            meme.value = memeRepository.getMeme()
        }
        return meme.value
    }
}
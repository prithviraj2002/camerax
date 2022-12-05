package com.example.memeplay.repository

import com.example.memeplay.model.Meme
import com.example.memeplay.network.MemeApi
import javax.inject.Inject

class MemeRepository @Inject constructor(private val memeApi: MemeApi) {
    suspend fun getMeme(): Meme {
        return memeApi.getMeme()
    }
}
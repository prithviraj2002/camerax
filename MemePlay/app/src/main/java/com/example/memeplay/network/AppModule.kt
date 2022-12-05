package com.example.memeplay.network

import com.example.memeplay.model.Meme
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface MemeApi {
    @GET
    suspend fun getMeme() : Meme
}
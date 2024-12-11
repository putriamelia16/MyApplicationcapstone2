package com.dicoding.myapplicationcapstone.data.api

import com.dicoding.myapplicationcapstone.data.model.ChatRequest
import com.dicoding.myapplicationcapstone.data.model.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {


    @POST("v1/projects/241705916714/locations/us-central1/endpoints/7520317585871601664:generateContent")
    suspend fun generateChatResponse(
        @Header("Authorization") authorization: String,
        @Body chatRequest: ChatRequest
    ): Response<ChatResponse>
}
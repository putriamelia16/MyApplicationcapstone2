package com.dicoding.myapplicationcapstone.data.model

data class ChatResponse(
    val contents: ContentsResponse
)

data class ContentsResponse(
    val role: String,
    val parts: PartsResponse
)

data class PartsResponse(
    val text: String
)

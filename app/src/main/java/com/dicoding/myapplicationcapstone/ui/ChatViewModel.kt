package com.dicoding.myapplicationcapstone.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.myapplicationcapstone.data.api.ApiConfig
import com.dicoding.myapplicationcapstone.data.ChatRequest
import com.dicoding.myapplicationcapstone.data.Parts
import com.dicoding.myapplicationcapstone.data.model.Chat
import com.dicoding.myapplicationcapstone.data.toData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    private val _chatResponse = MutableLiveData<Chat>()
    val chatResponse: LiveData<Chat> = _chatResponse
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun sendChatRequest(prompt: String) {
        val chatRequest = ChatRequest(
            contents = com.dicoding.myapplicationcapstone.data.Contents(
                role = "user",
                parts = Parts(text = prompt)
            ),
        )
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = withContext(Dispatchers.IO) { ApiConfig.getAuthToken() }
                Log.d("ChatViewModel", "Token: $token")

                if (!token.isNullOrEmpty()) {
                    val chatContentResponse = withContext(Dispatchers.IO) {
                        ApiConfig.getApiService().generateChatResponse(
                            authorization = "Bearer $token",
                            chatRequest = chatRequest
                        )
                    }

                    Log.d("ChatViewModel", "Response: $chatContentResponse")

                    chatContentResponse?.let {
                        _chatResponse.value = it.toData()
                    }
                } else {
                    _error.value = "Failed to retrieve a valid token."
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred."
                Log.e("ChatViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
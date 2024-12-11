package com.dicoding.myapplicationcapstone.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myapplicationcapstone.data.api.ApiConfig
import com.dicoding.myapplicationcapstone.data.model.ChatRequest
import com.dicoding.myapplicationcapstone.data.model.Contents
import com.dicoding.myapplicationcapstone.data.model.GenerationConfig
import com.dicoding.myapplicationcapstone.data.model.Parts
import com.dicoding.myapplicationcapstone.data.model.PartsItem
import com.dicoding.myapplicationcapstone.data.model.SafetySettings
import com.dicoding.myapplicationcapstone.data.model.SystemInstruction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {
    private val _chatResponse = MutableLiveData<String>()
    val chatResponse: LiveData<String> = _chatResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun sendChatRequest(prompt: String) {
        val chatRequest = ChatRequest(
            contents = Contents(
                role = "user",
                parts = Parts(text = prompt)
            ),
            system_instruction = SystemInstruction(
                parts = listOf(PartsItem(text = "You should classify the text into one of the following classes: [gym, health, nutrition]"))
            ),
            safety_settings = SafetySettings(
                category = "HARM_CATEGORY_DANGEROUS_CONTENT",
                threshold = "BLOCK_NONE"
            ),
            generation_config = GenerationConfig(
                temperature = 0.7,
                topP = 0.5,
                topK = 3,
                candidateCount = 1,
                maxOutputTokens = 30,
                stopSequences = emptyList()
            )
        )

        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            try {
                val token = withContext(Dispatchers.IO) { ApiConfig.getAuthToken() }
                Log.d("ChatViewModel", "Token: $token")  // Debug log untuk token

                if (!token.isNullOrEmpty()) {
                    val response = withContext(Dispatchers.IO) {
                        ApiConfig.getApiService().generateChatResponse(
                            authorization = "Bearer $token",
                            chatRequest = chatRequest
                        )
                    }

                    if (response.isSuccessful) {
                        val generatedContent = response.body()?.contents?.parts?.text
                        Log.d(
                            "ChatViewModel",
                            "Generated Content: $generatedContent"
                        ) // Debug log untuk konten

                        generatedContent?.let {
                            _chatResponse.value = it
                        }
                    } else {
                        _error.value =
                            "Failed to generate content: ${response.errorBody()?.string()}"
                    }
                } else {
                    _error.value = "Failed to retrieve a valid token."
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred."
                Log.e("ChatViewModel", "Error: ${e.message}")  // Log error
            } finally {
                _isLoading.value = false
            }
        }
    }
}

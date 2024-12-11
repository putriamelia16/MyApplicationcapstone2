package com.dicoding.myapplicationcapstone.data.model


data class ChatRequest(
    val contents: Contents,
    val system_instruction: SystemInstruction,
    val safety_settings: SafetySettings,
    val generation_config: GenerationConfig
)

data class Contents(
    val role: String,
    val parts: Parts
)

data class Parts(
    val text: String
)

data class SystemInstruction(
    val parts: List<PartsItem>
)

data class PartsItem(
    val text: String
)

data class SafetySettings(
    val category: String,
    val threshold: String
)

data class GenerationConfig(
    val temperature: Double,
    val topP: Double,
    val topK: Int,
    val candidateCount: Int,
    val maxOutputTokens: Int,
    val stopSequences: List<String>
)

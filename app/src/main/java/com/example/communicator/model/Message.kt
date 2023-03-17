package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val content: String,
    val sendingTime: Long,
    val participation: Participation
)

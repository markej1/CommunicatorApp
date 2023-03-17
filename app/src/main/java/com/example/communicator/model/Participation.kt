package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class Participation(
    val id: String,
    val user: User,
    val conversation: Conversation
)

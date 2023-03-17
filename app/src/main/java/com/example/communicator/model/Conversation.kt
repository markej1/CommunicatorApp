package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val creationTime: Long,
    val name: String
)

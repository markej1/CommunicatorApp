package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val userId: String,
    val token: String
)

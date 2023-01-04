package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

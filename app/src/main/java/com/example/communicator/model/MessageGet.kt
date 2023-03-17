package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageGet(
    val id: String,
    val content: String,
    val sendingTime: Long,
    val Participation: ParticipationGet
)

@Serializable
data class ParticipationGet(
    val id: String,
    val User: SafeUser
)

@Serializable
data class SafeUser(
    val id: String,
    val login: String,
    val firstName: String,
    val lastName: String
)

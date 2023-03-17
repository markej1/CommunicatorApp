package com.example.communicator.model

import kotlinx.serialization.Serializable

@Serializable
data class ConvPart(
    val conversation: Conversation,
    val participation: ParticipationId
)

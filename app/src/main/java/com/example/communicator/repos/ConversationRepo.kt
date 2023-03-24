package com.example.communicator.repos

import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.Conversation
import com.example.communicator.model.Participation
import com.example.communicator.model.Token
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class ConversationRepo {

//    TODO: Choose ip address and port
    private val ipAddress: String = ""
    private val port = ""

    suspend fun getConversations(token: Token): ArrayList<Conversation> {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("http://$ipAddress:$port/conversation") {
            headers {
                append("userId", token.userId)
                append("token", token.token)
            }
        }
        when (response.status.value) {
            200 -> return response.body() as ArrayList<Conversation>
            else -> throw InternalServerException()
        }
    }

    suspend fun addConversations(token: Token, conversation: Conversation, login: String,
                                 participation: Participation): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val messageUrl = "http://$ipAddress:$port/conversation/" + conversation.id + "/" + login
        val response = client.post(messageUrl) {
            headers {
                append("userId", token.userId)
                append("token", token.token)
            }
            contentType(ContentType.Application.Json)
            setBody(participation)
        }
        when (response.status.value) {
            201 -> return true
            else -> throw InternalServerException()
        }

    }

}
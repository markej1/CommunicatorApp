package com.example.communicator.repos

import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class MessageRepo {

//    TODO: Choose ip address and port
    private val ipAddress: String = ""
    private val port = ""

    suspend fun getMessages(token: Token, conversation: Conversation): ArrayList<MessageGet> {
        val messageUrl = "http://$ipAddress:$port/conversation/message/" + conversation.id
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get(messageUrl) {
            headers {
                append("userId", token.userId)
                append("token", token.token)
            }
        }
        when (response.status.value) {
            200 -> return response.body() as ArrayList<MessageGet>
            else -> throw InternalServerException()
        }
    }

    suspend fun addMessage(token: Token, conversation: Conversation, message: Message): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val messageUrl = "http://$ipAddress:$port/conversation/message/" + conversation.id
        val response = client.post(messageUrl) {
            headers {
                append("userId", token.userId)
                append("token", token.token)
            }
            contentType(ContentType.Application.Json)
            setBody(message)
        }
        when (response.status.value) {
            201 -> return true
            else -> throw InternalServerException()
        }

    }

}
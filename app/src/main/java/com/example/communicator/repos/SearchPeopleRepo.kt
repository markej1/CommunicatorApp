package com.example.communicator.repos

import android.util.Log
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class SearchPeopleRepo {

    suspend fun addConversation(token: Token, convPart: ConvPart): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("http://192.168.8.100:8000/conversation") {
            headers {
                append("userId", token.userId)
                append("token", token.token)
            }
            contentType(ContentType.Application.Json)
            setBody(convPart)
            Log.i("headerbody", headers["userId"].toString())
            Log.i("headerbody", headers["token"].toString())
            Log.i("headerbody", body.toString())
        }
        when (response.status.value) {
            201 -> return true
            else -> throw InternalServerException()
        }
    }

}
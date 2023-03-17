package com.example.communicator.repos

import android.util.Log
import com.example.communicator.exceptions.ConflictException
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.exceptions.NotAuthorizedException
import com.example.communicator.model.Token
import com.example.communicator.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class AuthRepo {
    suspend fun login(login: String, pass: String): Token {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("http://192.168.8.100:8000/auth/login") {
            headers {
                append("login", login)
                append("password", pass)
            }
            Log.i("headerbody", headers["login"].toString())
        }
        when (response.status.value) {
            200 -> return response.body() as Token
            404 -> throw NotAuthorizedException()
            else -> throw InternalServerException()
        }
    }

    suspend fun register(user: User): Boolean {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("http://192.168.8.100:8000/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(user)
            Log.i("headerbody", body.toString())
        }
        when (response.status.value) {
            201 -> return true
            409 -> throw ConflictException()
            else -> throw InternalServerException()
        }

    }
}
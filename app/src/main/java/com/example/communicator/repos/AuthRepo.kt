package com.example.communicator.repos

import android.content.Context
import android.widget.Toast
import com.example.communicator.exceptions.ConflictException
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.exceptions.NotAuthorizedException
import com.example.communicator.model.Token
import com.example.communicator.model.User
import com.example.communicator.ui.register.RegisterActivity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class AuthRepo {
    //    sprawdzic wczesniej, ze na pewno ani login ani pass nie sa nullami
    suspend fun login(login: String, pass: String): Token {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("https://happy-falcon-53.loca.lt/auth/login") {
            headers {
                append("login", login)
                append("password", pass)
            }
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
        val response = client.post("https://happy-falcon-53.loca.lt/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        when (response.status.value) {
            201 -> return true
            409 -> throw ConflictException()
            else -> throw InternalServerException()
        }

    }
}
package com.example.communicator.repos

import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.exceptions.NotAuthorizedException
import com.example.communicator.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

class SearchPeopleRepo {

    suspend fun findPeople(login: String): User {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get() {
            headers {
                append("login", login)
            }
        }
        when (response.status.value) {
            200 -> return response.body() as User
            404 -> throw NotAuthorizedException()
            else -> throw InternalServerException()
        }
    }

}
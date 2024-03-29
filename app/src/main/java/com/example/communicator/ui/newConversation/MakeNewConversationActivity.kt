package com.example.communicator.ui.newConversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.communicator.R
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.*
import com.example.communicator.repos.SearchPeopleRepo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.util.*

class MakeNewConversationActivity : AppCompatActivity() {

    private lateinit var  token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_new_conversation)

        val tokenJson = intent.getStringExtra("token")
        val type: Type = object : TypeToken<Token>() {}.type
        token = Gson().fromJson<Any>(tokenJson, type) as Token

        val editLogin = findViewById<EditText>(R.id.edit_conversation_name)
        val searchPeopleRepo = SearchPeopleRepo()
        val buttonMake = findViewById<Button>(R.id.button_add_conversation)

        buttonMake.setOnClickListener {
            val newConversation = Conversation(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                editLogin.text.toString()
            )
            val newParticipationId = ParticipationId(
                UUID.randomUUID().toString()
            )

            val newConvPart = ConvPart(newConversation, newParticipationId)
            val newConvPartJson = Gson().toJson(newConvPart)

            Log.d("json", newConvPartJson.toString())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    searchPeopleRepo.addConversation(token, newConvPart)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MakeNewConversationActivity,
                            "Dodano nową konwersację",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                catch (e: InternalServerException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MakeNewConversationActivity,
                            "Błąd serwera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }

}
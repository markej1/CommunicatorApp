package com.example.communicator.ui.newConversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.communicator.R
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.Conversation
import com.example.communicator.model.Participation
import com.example.communicator.model.Token
import com.example.communicator.model.User
import com.example.communicator.repos.ConversationRepo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.util.*

class AddFriendsActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var conversation: Conversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        val tokenJson = intent.getStringExtra("token")
        val type: Type = object : TypeToken<Token>() {}.type
        token = Gson().fromJson<Any>(tokenJson, type) as Token

        val convJson = intent.getStringExtra("conversation")
        val typeC: Type = object : TypeToken<Conversation>() {}.type
        conversation = Gson().fromJson<Any>(convJson, typeC) as Conversation

        val addButton = findViewById<Button>(R.id.button_make)
        val conversationRepo = ConversationRepo()
        val loginFriend = findViewById<EditText>(R.id.edit_login)

        addButton.setOnClickListener {
            val part = Participation(UUID.randomUUID().toString(),
                User("","","","",""),
                Conversation("",0,"")
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    conversationRepo.addConversations(token, conversation,
                        loginFriend.text.toString(), part)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@AddFriendsActivity,
                            "Dodano!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                }
                catch (e: InternalServerException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@AddFriendsActivity,
                            "Błąd serwera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }
}
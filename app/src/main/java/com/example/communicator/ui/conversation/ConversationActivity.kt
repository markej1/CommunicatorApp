package com.example.communicator.ui.conversation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.*
import com.example.communicator.repos.MessageRepo
import com.example.communicator.ui.newConversation.AddFriendsActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class ConversationActivity : AppCompatActivity() {

    private lateinit var  token: Token
    private lateinit var conversation: Conversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenJson = intent.getStringExtra("token")
        val type: Type = object : TypeToken<Token>() {}.type
        token = Gson().fromJson<Any>(tokenJson, type) as Token

        val convJson = intent.getStringExtra("conversation")
        val typeC: Type = object : TypeToken<Conversation>() {}.type
        conversation = Gson().fromJson<Any>(convJson, typeC) as Conversation

        title = conversation.name

        setContentView(R.layout.activity_conversation)

        val messageRepo = MessageRepo()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_messages)

        val sendButton = findViewById<ImageButton>(R.id.send_button)
        val refreshButton = findViewById<ImageButton>(R.id.refresh_button)
        val myMessage = findViewById<EditText>(R.id.my_message)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val messages: ArrayList<MessageGet> = messageRepo.getMessages(token, conversation)
                withContext(Dispatchers.Main) {
                    recyclerView.layoutManager =
                        LinearLayoutManager(this@ConversationActivity)
                    recyclerView.adapter = MessagesAdapter(messages, token.userId)
                }

            }
            catch (e: InternalServerException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ConversationActivity,
                        "Błąd serwera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        sendButton.setOnClickListener {
            val mess = Message(UUID.randomUUID().toString(),
                myMessage.text.toString(),
                System.currentTimeMillis(),
                Participation(
                    "",
                    User("","","","",""),
                    Conversation("",0,"")
                )
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    messageRepo.addMessage(token, conversation, mess)
                }
                catch (e: InternalServerException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ConversationActivity,
                            "Błąd serwera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        refreshButton.setOnClickListener {
            recreate()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_in_conversation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.option_add_people_to_conversation -> {
                val intentAddFriends =
                    Intent(this, AddFriendsActivity::class.java)
                intentAddFriends.putExtra("token", Gson().toJson(token))
                intentAddFriends.putExtra("conversation", Gson().toJson(conversation))
                startActivity(intentAddFriends)
            }
        }
        return false
    }
}
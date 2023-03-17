package com.example.communicator.ui.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.Conversation
import com.example.communicator.model.Token
import com.example.communicator.repos.ConversationRepo
import com.example.communicator.ui.conversation.ConversationActivity
import com.example.communicator.ui.newConversation.MakeNewConversationActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class MenuActivity : AppCompatActivity(), OnConversationClickListener {

    private lateinit var  token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val tokenJson = intent.getStringExtra("token")
        val type: Type = object : TypeToken<Token>() {}.type
        token = Gson().fromJson<Any>(tokenJson, type) as Token

        val conversationRepo = ConversationRepo()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val conversations = conversationRepo.getConversations(token)
                withContext(Dispatchers.Main) {
                    if (conversations.size == 0) {
                        val letterImg = findViewById<ImageView>(R.id.image_letter)
                        val noConvText = findViewById<TextView>(R.id.no_conversations_textview)
                        letterImg.visibility = View.VISIBLE
                        noConvText.visibility = View.VISIBLE
                    } else {
                        val conversationsList = conversations.sortedBy { it.name }
                        val conversationsSorted = ArrayList<Conversation>(conversationsList)
                        recyclerView.layoutManager = LinearLayoutManager(this@MenuActivity)
                        recyclerView.adapter =
                            ConversationAdapter(
                                conversationsSorted,
                                this@MenuActivity
                            )
                    }
                }

            }
            catch (e: InternalServerException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MenuActivity,
                        "Błąd serwera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.standard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.option_make_conversation -> {
                val intentSearch =
                    Intent(this, MakeNewConversationActivity::class.java)
                intentSearch.putExtra("token", Gson().toJson(token))
                startActivity(intentSearch)
            }
        }
        return false
    }

    override fun onConversationClick(conversation: Conversation) {
        val intentConversation = Intent(this, ConversationActivity::class.java)
        intentConversation.putExtra("token", Gson().toJson(token))
        intentConversation.putExtra("conversation", Gson().toJson(conversation))
        startActivity(intentConversation)
    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }

}
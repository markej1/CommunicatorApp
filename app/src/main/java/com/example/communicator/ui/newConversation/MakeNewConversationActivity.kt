package com.example.communicator.ui.newConversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.exceptions.ConflictException
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.exceptions.NotAuthorizedException
import com.example.communicator.model.User
import com.example.communicator.repos.SearchPeopleRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MakeNewConversationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_new_conversation)

        val listOfSearchPeople: ArrayList<User> =  arrayListOf()
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_search_people)
        val editLogin = findViewById<EditText>(R.id.editLogin)
        val searchPeopleRepo = SearchPeopleRepo()
        val buttonMake = findViewById<Button>(R.id.button_make)

        buttonAdd.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val findUser = searchPeopleRepo.findPeople(editLogin.text.toString())
                    listOfSearchPeople.add(findUser)
                    withContext(Dispatchers.Main) {
                        recyclerView.layoutManager =
                            LinearLayoutManager(this@MakeNewConversationActivity)
                        recyclerView.adapter = SearchPeopleAdapter(listOfSearchPeople)
                    }
                }
                catch (e: NotAuthorizedException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MakeNewConversationActivity,
                            "Nie istnieje osoba o takim loginie",
                            Toast.LENGTH_SHORT).show()
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

        buttonMake.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    searchPeopleRepo.addPeople(listOfSearchPeople)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MakeNewConversationActivity,
                            "Dodano nową konwersację",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                catch (e: ConflictException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MakeNewConversationActivity,
                            "Nie udało się dodać tych osób",
                            Toast.LENGTH_SHORT).show()
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
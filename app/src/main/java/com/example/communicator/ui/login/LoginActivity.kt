package com.example.communicator.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.communicator.R
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.exceptions.NotAuthorizedException
import com.example.communicator.repos.AuthRepo
import com.example.communicator.ui.menu.MenuActivity
import com.example.communicator.ui.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.button_login)
        val lg = findViewById<EditText>(R.id.input_login)
        val pwd = findViewById<EditText>(R.id.input_password)
        val wrongDataWarning = findViewById<TextView>(R.id.text_wrong_data)

        val authRepo = AuthRepo()
        val intentMenu = Intent(this@LoginActivity, MenuActivity::class.java)

        buttonLogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val token = authRepo.login(lg.text.toString(), pwd.text.toString())

                    val data = getSharedPreferences("authData", Context.MODE_PRIVATE)
                    val editor = data.edit()
                    editor.putString("user_id", token.userId)
                    editor.putString("token", token.token)
                    editor.apply()

                    withContext(Dispatchers.Main) {
                        intentMenu.putExtra("token", Gson().toJson(token))
                        startActivity(intentMenu)
                    }

                }
                catch (e: NotAuthorizedException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity,
                            "Niepoprawne dane logowania",
                            Toast.LENGTH_SHORT).show()
                        wrongDataWarning.text = "Niepoprawne dane logowania."
                    }
                }
                catch (e: InternalServerException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Błąd serwera",
                            Toast.LENGTH_SHORT
                        ).show()
                        wrongDataWarning.text = "Błąd serwera."
                    }
                }
            }
        }

        val buttonRegister = findViewById<Button>(R.id.button_register)
        val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)

        buttonRegister.setOnClickListener {
            startActivity(intentRegister)
        }

    }
}
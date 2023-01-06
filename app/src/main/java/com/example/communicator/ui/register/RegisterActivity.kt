package com.example.communicator.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import com.example.communicator.R
import com.example.communicator.exceptions.ConflictException
import com.example.communicator.exceptions.InternalServerException
import com.example.communicator.model.User
import com.example.communicator.repos.AuthRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val hideButton = findViewById<ToggleButton>(R.id.toggleButton)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        hideButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else {
                editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val buttonSubmit = findViewById<Button>(R.id.button_submit)
//        val intentLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
        val editName = findViewById<EditText>(R.id.editName)
        val editSurname = findViewById<EditText>(R.id.editSurname)
        val editLogin = findViewById<EditText>(R.id.editLogin)

        val authRepo = AuthRepo()

        buttonSubmit.setOnClickListener {
            val newUser = User(
                UUID.randomUUID().toString(),
                editLogin.text.toString(),
                editPassword.text.toString(),
                editName.text.toString(),
                editSurname.text.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    authRepo.register(newUser)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Zarejestrowano!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
                catch (e: ConflictException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Złe dane",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                catch (e: InternalServerException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Błąd serwera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }

//    fun finishActivity(view: View) { finish() }

}
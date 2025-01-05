package com.example.fastnotes.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fastnotes.data.DBHelper
import com.example.fastnotes.databinding.ActivityLoginBinding
import com.example.fastnotes.model.User

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = application.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val userNameApi = sharedPreferences.getString("userName","")

        if (userNameApi != null){
            if (userNameApi.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.btnLogin.setOnClickListener {
            val  userName = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val checked = binding.checkboxLogin.isChecked
            if (userName.isNotEmpty()&& password.isNotEmpty()){
                val dbHelper = DBHelper(this)
                val result = dbHelper.getAUser(userName, password)
                if (result.success){
                    val userId = result.data as? Int
                    if (userId != null) { // Verifica se o ID não é nulo
                        val userContext = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
                        with(userContext.edit()) {
                            putString("userName", userName)
                            putInt("id", userId) // Salva o ID apenas se não for nulo
                            apply()
                        }

                        if (checked) {
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("userName", userName)
                            editor.putInt("id", userId) // Salva o ID apenas se não for nulo
                            editor.apply()
                        }

                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "ID do usuário inválido.", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Por favor, preencha os campos!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCreateARegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
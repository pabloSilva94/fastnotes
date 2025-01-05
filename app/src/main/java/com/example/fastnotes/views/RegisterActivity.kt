package com.example.fastnotes.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fastnotes.data.DBHelper
import com.example.fastnotes.databinding.ActivityRegisterBinding
import com.example.fastnotes.model.User

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToBack.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            val userName = binding.editRegisterUsername.text.toString()
            val userPassword = binding.editRegisterPassword.text.toString()
            if (userName.isNotEmpty()&&userPassword.isNotEmpty()){
                val user = User(name = userName, password = userPassword)
                val dbHelper = DBHelper(this)
                val result = dbHelper.insertUser(user)
                if (result.success){
                    Toast.makeText(this,result.message, Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Por favor, preencha o nome!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
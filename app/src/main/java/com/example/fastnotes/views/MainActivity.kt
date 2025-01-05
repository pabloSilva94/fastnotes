package com.example.fastnotes.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastnotes.R
import com.example.fastnotes.databinding.ActivityMainBinding
import com.example.fastnotes.model.Task
import com.example.fastnotes.adapter.TaskListAdapter
import com.example.fastnotes.data.DBHelper
import com.example.fastnotes.repository.TaskRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskListAdapter
    private lateinit var taskRepository: TaskRepository
    private  var tasks:MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuário")
        val userId = sharedPref.getInt("id",0)
        taskRepository = TaskRepository(DBHelper(this))
        binding.textHome.text="Olá, $userName!"
        val tasks = ArrayList(taskRepository.getAllTasks(userId))
        adapter = TaskListAdapter(tasks, TaskListAdapter.OnClickListener(
            onClick = { task ->
                println("Item clicked: ${task.title}")
            },
            onEditClick = { task ->
                println("Edit button clicked for: ${task.title}")
                val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
                val dialog = androidx.appcompat.app.AlertDialog.Builder(this).setView(dialogView).create()
                val editTitle = dialogView.findViewById<EditText>(R.id.edit_title)
                val editDescription = dialogView.findViewById<EditText>(R.id.edit_description)
                editTitle.setText(task.title)
                editDescription.setText(task.description)
                val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
                val btnSave = dialogView.findViewById<Button>(R.id.btn_save)
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnSave.setOnClickListener {
                    val title = editTitle.text.toString()
                    val description = editDescription.text.toString()

                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        task.title = title
                        task.description = description
                        val result = taskRepository.updateTask(task)

                        if (result.success){
                            tasks.clear()
                            tasks.addAll((taskRepository.getAllTasks(userId)))
                            adapter.notifyDataSetChanged()
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }else{
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show()
            },
            onDeleteClick = { task, _ ->
                if (task.id != null) {
                    val result = taskRepository.deleteTask(task)
                    if (result.success) {
                        tasks.clear()
                        tasks.addAll(taskRepository.getAllTasks(userId))
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Tarefa sem ID válido para exclusão", Toast.LENGTH_SHORT).show()
                }
            }
        ))

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.btnAddTask.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this).setView(dialogView).create()
            val editTitle = dialogView.findViewById<EditText>(R.id.edit_title)
            val editDescription = dialogView.findViewById<EditText>(R.id.edit_description)
            val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
            val btnSave = dialogView.findViewById<Button>(R.id.btn_save)

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnSave.setOnClickListener {
                val title = editTitle.text.toString()
                val description = editDescription.text.toString()

                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val newTask = Task(id=null, title = title, description = description, user_id = userId)
                    val result = taskRepository.addTask(newTask)
                    if (result.success){
                        tasks.clear()
                        tasks.addAll(taskRepository.getAllTasks(userId))
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        adapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(this, "Erro ao adicionar registro: ${result.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }
        binding.btnExit.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("userName","")
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.textMsg.text = getMsgDodia()
    }

    private fun  getMsgDodia():String{
        val currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return  when (currHour){
            in 6..11 -> "Tenha um ótimo dia!"
            in 11..17 -> "Tenha uma boa tarde!"
            in 17..22 -> "Tenha uma boa noite!"
            else -> "Tenha uma boa madrugada!"
        }
    }
}
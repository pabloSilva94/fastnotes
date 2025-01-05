package com.example.fastnotes.repository

import com.example.fastnotes.data.DBHelper
import com.example.fastnotes.model.Result
import com.example.fastnotes.model.Task

class TaskRepository(private val dbHelper: DBHelper) {
    fun addTask(task: Task):Result{
        return dbHelper.insertTask(task)
    }
    fun getAllTasks(userId:Int): List<Task> {
        return dbHelper.getTasksForUser(userId)
    }
    fun updateTask(task: Task): Result {
        return dbHelper.updateTask(task)
    }
    fun deleteTask(task: Task):Result{
        return dbHelper.deleteTask(task)
    }

}
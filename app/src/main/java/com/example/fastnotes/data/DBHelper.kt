package com.example.fastnotes.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fastnotes.model.Result
import com.example.fastnotes.model.Task
import com.example.fastnotes.model.User

class DBHelper(context: Context) : SQLiteOpenHelper(context, "fastnotes.db", null, 1){
    companion object {
        private const val CREATE_USER_TABLE = """
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """

        private const val CREATE_TASK_TABLE = """
            CREATE TABLE task (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                user_id INTEGER,
                FOREIGN KEY(user_id) REFERENCES user(id)
            )
        """
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_TASK_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS task")
        db.execSQL("DROP TABLE IF EXISTS user")
        onCreate(db)
    }

    fun insertUser(user: User):Result{
        val db = this.writableDatabase
        return try {
            val id = db.insert("user",null, user.toContentValues())
            if (id == -1L){
                throw Exception("Erro ao inserir usuário")
            }
            db.close()
            Result(success = true, message = "Usuário inserido com sucesso")
        }catch (e: Exception) {
            db.close()
            Result(success = false, message = "${e.message}")
        }
    }
    fun getAUser(name:String,  password: String):Result{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user WHERE name=? AND password=?", arrayOf(name,password))
        return try {
            if (cursor.count == 1){
                if (cursor.moveToFirst()){
                    val user = User.fromCursor(cursor)
                    db.close()
                    Result(success = true, message = "Seja bem vindo, ${user.name}", data = user.id)
                }else{
                    db.close()
                    Result(success = false, message = "Usuário ou senha incorretos")
                }
            }else{
                Result(success = false, message = "Dados não encontrados")
            }
        } catch (e: Exception) {
            db.close()
            Result(success = false, message = "Erro: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun insertTask(task: Task):Result{
        val db = this.writableDatabase
        return try {
            val id = db.insert("task", null ,task.toContentValues())
            if (id == -1L){
                throw Exception("Erro ao inserir o dado")
            }
            Result(success = true, message = "Cadastro realizado com sucesso")
        }catch (e: Exception) {
            Result(success = false, message = "${e.message}")
        }finally {
            db.close()
        }
    }
    fun getTasksForUser(userId:Int):List<Task>{
        val db = this.readableDatabase
        val taskList = mutableListOf<Task>()
        val cursor = db.rawQuery("SELECT * FROM task WHERE user_id = ?", arrayOf(userId.toString()))
        try {
            if (cursor.moveToFirst()){
                do {
                    val  task = Task.fromCursor(cursor)
                    taskList.add(task)
                }while (cursor.moveToNext())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            cursor.close()
            db.close()
        }
        return taskList
    }
    fun updateTask(task: Task):Result{
        val db = this.writableDatabase
       return  try {
           val contentValues = ContentValues().apply {
               put("description", task.description)
               put("title", task.title)
           }
           val rowsAffected = db.update("task", contentValues, "id = ?", arrayOf(task.id.toString()))
           if (rowsAffected > 0){
               Result(success = true, message = "Alterado com sucesso", data = task)
           }else{
               Result(success = false, message = "Erro ao atualizar a tarefa. Tarefa não encontrada.")
           }
       } catch (e: Exception){
           Result(success = false, message = "Erro ao atualizar a tarefa: ${e.message}")
       }finally {
           db.close()
       }
    }
    fun deleteTask(task: Task): Result {
        if (task.id == null) {
            return Result(success = false, message = "inválido")
        }

        val db = this.writableDatabase
        return try {
            val rowsAffected = db.delete("task", "id = ?", arrayOf(task.id.toString()))
            if (rowsAffected > 0) {
                Result(success = true, message = "Registro deletada com sucesso", data = task)
            } else {
                Result(success = false, message = "Erro ao deletar o registro")
            }
        } catch (e: Exception) {
            Result(success = false, message = "Erro ao deletar o registro: ${e.message}")
        } finally {
            db.close()
        }
    }
}
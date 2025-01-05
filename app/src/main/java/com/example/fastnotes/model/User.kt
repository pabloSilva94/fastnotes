package com.example.fastnotes.model

import android.content.ContentValues
import android.database.Cursor

data class User(var id: Int = 0, var name:String, var password:String) {
    fun toContentValues():ContentValues{
        val values = ContentValues()
        values.put("id", id)
        values.put("name", name)
        values.put("password", password)
        return  values
    }
    companion object{
        fun fromCursor(cursor: Cursor):User{
            return User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            )
        }
    }
}
package com.example.fastnotes.model

import android.content.ContentValues
import android.database.Cursor

data class Task(var id: Int?=null, var title:String, var description:String, var user_id:Int ) {
    fun toContentValues():ContentValues{
        val values = ContentValues()
        values.put("id", id)
        values.put("title", title)
        values.put("description", description)
        values.put("user_id", user_id)
        return values
    }
    companion object{
        fun fromCursor(cursor: Cursor):Task{
            return Task(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
            )
        }
    }
}
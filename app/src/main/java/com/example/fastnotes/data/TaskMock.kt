package com.example.fastnotes.data

import com.example.fastnotes.model.Task

class TaskMock {
    var listTask = ArrayList<Task>()
    init {
        for (i in 0..-1){
            listTask.add(Task(i,i.toString(), "Teste de descrição", user_id = 1))
        }
    }

}
package com.example.fastnotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastnotes.model.Task
import com.example.fastnotes.R

class TaskListAdapter(
    val listTask: ArrayList<Task>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    // ViewHolder para cada item da RecyclerView
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_model)
        val descriptionView: TextView = itemView.findViewById(R.id.text_description)
        val editButton: Button = itemView.findViewById(R.id.btn_editTask)
        val btnDelete: Button = itemView.findViewById(R.id.btn_deleteTask)
    }

    // Métodos do Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = listTask[position]
        holder.textView.text = task.title
        holder.descriptionView.text = task.description

        // Ações para clique no item
        holder.itemView.setOnClickListener {
            onClickListener.onClick(task)
        }

        // Ações para clicar no botão Editar
        holder.editButton.setOnClickListener {
            onClickListener.onEditClick(task)
        }

        // Ações para clicar no botão Deletar
        holder.btnDelete.setOnClickListener {
            onClickListener.onDeleteClick(task, position)
        }
    }

    // Listener de eventos para cliques
    class OnClickListener(
        val onClick: (task: Task) -> Unit,
        val onEditClick: (task: Task) -> Unit,
        val onDeleteClick: (task: Task, position: Int) -> Unit
    )
}

package com.example.newsflash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tasks : Fragment() {

    private lateinit var todoList: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val todos: MutableList<String> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        todoList = view.findViewById(R.id.todoList)

        val layoutManager = LinearLayoutManager(context)
        todoList.layoutManager = layoutManager

        todoAdapter = TodoAdapter(todos)
        todoList.adapter = todoAdapter

        val taskEditText: EditText = view.findViewById(R.id.taskEditText)
        val addButton: Button = view.findViewById(R.id.addButton)

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)


        loadTasks()


        addButton.setOnClickListener {
            val task = taskEditText.text.toString()
            if (task.isNotEmpty()) {
                addTask(task)
                taskEditText.text.clear()
            }
        }

        return view
    }

    private fun addTask(task: String) {
        todos.add(task)
        todoAdapter.notifyItemInserted(todos.size - 1)
        saveTasks()
    }

    fun removeTask(position: Int) {
        todos.removeAt(position)
        todoAdapter.notifyItemRemoved(position)
        saveTasks()
    }


    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", todos.toSet())
        editor.apply()
    }


    private fun loadTasks() {
        val taskSet = sharedPreferences.getStringSet("tasks", setOf())
        todos.addAll(taskSet ?: emptySet())
        todoAdapter.notifyDataSetChanged()
    }

    inner class TodoAdapter(private val todos: List<String>) :
        RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
            return TodoViewHolder(view)
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val task = todos[position]
            holder.bind(task)
        }

        override fun getItemCount(): Int {
            return todos.size
        }

        inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val todoText: TextView = itemView.findViewById(R.id.todoText)
            private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

            fun bind(task: String) {
                todoText.text = task

                deleteButton.setOnClickListener {
                    removeTask(adapterPosition)
                }
            }
        }
    }
}

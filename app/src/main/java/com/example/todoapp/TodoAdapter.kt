package com.example.todoapp

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.icu.text.CaseMap
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View //View视图：(TextView,Button,ImageView)都是常用常见的视图.
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding

class TodoAdapter(
    // 使用MutableList可以進行增刪改查操作
    private val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    // ViewHolder：用於直觀地表示RecyclerView（一行）中資料清單中的元素。
    // 讓view來自item_todo
    // TodoViewHolder必須繼承RecyclerView的ViewHolder
    class TodoViewHolder(val itemTodoBinding: ItemTodoBinding) :
        RecyclerView.ViewHolder(itemTodoBinding.root)

    //onCreateViewHolder 是我們建立 TodoViewHolder 實體的地方，一般只需要直接回傳一個 ViewHolder 即可，系統會依據螢幕大小來呼叫我們的 function 決定建立幾個 ViewHolder。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TodoViewHolder(itemTodoBinding)
    }

    //在RecyclerView 提供所有必要的訊息-在ViewHolder 實例應顯示的資料，以及在資料列表中的（新）位置。
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemTodoBinding.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, cbDone.isChecked)
            cbDone.setOnCheckedChangeListener {_, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean){
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)

        //這方法我們需要傳入新增的位置，因此程式會知道從哪裡新增資料，不用重新建構 RecyclerView。
        // Insert new items to RecyclerView
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        todos.removeAll{ todo ->
            todo.isChecked
        }
        //這個方法會從新的 List 建構一個新的RecyclerView。
        notifyDataSetChanged()
    }
}
package com.example.mypocket.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mypocket.databinding.ItemExpenseBinding
import com.example.mypocket.model.Expense

class ExpenseListAdapter(expenseList: ArrayList<Expense>, internal val context: Context, private val callBack:(Int) -> Unit):
        RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>(){

    private var expenseList = ArrayList<Expense>()
    init {
        this.expenseList = expenseList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder{

        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemExpenseBinding.inflate(inflate, parent, false)

        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int){

        val expense = expenseList[position]

        holder.descrip.text = expense.descrip
        holder.value  .text = expense.value
        holder.date   .text = expense.date
        holder.parcel .text = expense.parcel

        holder.contraint.setOnClickListener {
            callBack(expense.id)
        }
    }

    inner class ExpenseViewHolder( private val binding: ItemExpenseBinding ): RecyclerView.ViewHolder(binding.root) {

        val contraint: ConstraintLayout = binding.constraintItem

        val descrip : TextView = binding.txtItemDescription
        val value   : TextView = binding.txtItemValue
        val date    : TextView = binding.txtItemDate
        val parcel  : TextView = binding.txtItemParcel

    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}

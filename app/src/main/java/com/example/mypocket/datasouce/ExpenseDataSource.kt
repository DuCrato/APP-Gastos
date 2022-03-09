package com.example.mypocket.datasouce

import com.example.mypocket.model.Expense

object ExpenseDataSource {

    private val list = arrayListOf<Expense>()

    fun getList() = list

    fun insertExpense(expense: Expense){

        list.add(expense.copy(id = list.size + 1))
    }

}
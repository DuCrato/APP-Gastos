package com.example.mypocket.ui

import android.app.Activity
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mypocket.R
import com.example.mypocket.dataBase.DataBaseHandler
import com.example.mypocket.databinding.ActivityRegisterBinding
import com.example.mypocket.datasouce.ExpenseDataSource
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import com.example.mypocket.extensions.format
import com.example.mypocket.extensions.text
import com.example.mypocket.model.Expense

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val dataBaseHandler = DataBaseHandler(this)
    var expense: Expense? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("mode") == "Edit"){ editExpense()
        }else{ saveExpense()}

        binding.btnSave.setOnClickListener {

            if(testData()){
                if(intent.getStringExtra("mode") == "Edit"){

                    expense = populateExpense(expense)
                    dataBaseHandler.updateExpense(expense!!)

                }else{

                    expense = populateExpense(null)
                    dataBaseHandler.addExpense(expense!!)
                }
                finish()

            }else{ Toast.makeText(this, R.string.erro_data,Toast.LENGTH_LONG).show()}
        }
    }

    private fun testData(): Boolean{
        return binding.editDescription.text.toString() != "" &&
                binding.editValue     .text.toString() != "" &&
                binding.editDate      .text.toString() != ""
    }

    private fun editExpense(){

        expense = dataBaseHandler.getExpense(intent.getIntExtra("id",0))

        binding.toolBar.title = getString(R.string.edit_state)
        binding.btnDelete.isVisible

        binding.editDescription.editText!!.setText(expense!!.descrip)
        binding.editValue      .editText!!.setText(expense!!.value)
        binding.editDate       .editText!!.setText(expense!!.date)
        binding.editParcel     .editText!!.setText(expense!!.parcel)

        binding.btnDelete.setOnClickListener {

            dataBaseHandler.deleteExpense(expense!!.id)
            finish()
        }
    }

    private fun populateExpense(cost: Expense?): Expense{

        val expense = Expense()

        if(cost != null) expense.id = cost.id

        expense.descrip = binding.editDescription.toString()
        expense.value   = binding.editValue      .toString()
        expense.date    = binding.editDate       .toString()
        expense.parcel  = binding.editParcel     .toString()

        return expense
    }

    private fun saveExpense(){

        binding.editDate.editText?.setOnClickListener{

            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {

                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * - 1

                binding.editDate.text = Date(it + offSet).format()

            }
            datePicker.show(supportFragmentManager,"DATE_PICKER_TEST")
        }

        binding.btnSave.setOnClickListener {

            val expense = Expense(
                descrip = binding.editDescription.text,
                value   = binding.editValue      .text,
                date    = binding.editValue      .text,
                parcel  = binding.editParcel     .text
            )
            ExpenseDataSource.insertExpense(expense)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
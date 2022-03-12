package com.example.mypocket.ui

import android.app.Activity
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    val dataBaseHandler = DataBaseHandler(this)
    var expense: Expense? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()

        if(intent.getStringExtra("mode") == "Edit"){

            expense = dataBaseHandler.getExpense(intent.getIntExtra("id",0))
            binding.toolBar.title = getString(R.string.edit_state)
            binding.btnDelete.isVisible

            binding.tilDescription.editText!!.setText(expense!!.descrip)
            binding.tilValor.editText!!.setText(expense!!.value)
            binding.tilDescription.editText!!.setText(expense!!.date)

            if(expense!!.parcel == null){}
        }

    }

    fun insertListeners(){

        binding.tilDate.editText?.setOnClickListener{

            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {

                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * - 1

                binding.tilDate.text = Date(it + offSet).format()

            }
            datePicker.show(supportFragmentManager,"DATE_PICKER_TEST")
        }

        binding.btnSave.setOnClickListener {

            val expense = Expense(
                descrip = binding.tilDescription.text,
                value = binding.tilValor.text,
                date = binding.tilDate.text,
                parcel = binding.tilParcel.text
            )
            ExpenseDataSource.insertExpense(expense)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
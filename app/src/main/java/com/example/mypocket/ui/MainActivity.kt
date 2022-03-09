package com.example.mypocket.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mypocket.R
import com.example.mypocket.databinding.ActivityMainBinding
import com.example.mypocket.datasouce.ExpenseDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { ExpenseListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
    }

    private fun insertListeners(){

        binding.floatNewExpense.setOnClickListener {

            startActivityForResult(Intent(this, RegisterActivity::class.java), CREATE_NEW_EXPENSE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_NEW_EXPENSE){

            binding.rvExpense.adapter = adapter
            adapter.submitList(ExpenseDataSource.getList())
        }
    }

    companion object {
        private const val CREATE_NEW_EXPENSE = 1000
    }
}
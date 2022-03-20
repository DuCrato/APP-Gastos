package com.example.mypocket.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import android.widget.SearchView
import com.example.mypocket.R
import com.example.mypocket.dataBase.DataBaseHandler
import com.example.mypocket.databinding.ActivityMainBinding
import com.example.mypocket.datasouce.ExpenseDataSource
import com.example.mypocket.model.Expense

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var expenseList = ArrayList<Expense>()
    var expenseListAdapter: ExpenseListAdapter? = null
    var linearLayoutManager: LinearLayout? = null
    var dataBaseHandler = DataBaseHandler(this)
    var search: Boolean = false
    var searchStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
    }

    private fun insertListeners(){

        binding.floatNewExpense.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{

        menuInflater.inflate(R.menu.tool_mounth, menu)

        val searchExpense = menu.findItem(R.id.search_expense)
        val searchView = searchExpense.actionView as SearchView

        searchView.queryHint = getString(R.string.search_expense)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0.toString().isNotEmpty()){

                    search = true
                    searchStr = p0!!

                }else{
                    search = false
                    searchStr = ""
                }
                initView()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initView(){

        expenseList = if(!search) dataBaseHandler.getExpenseList()
        else dataBaseHandler.searchExpense(searchStr)

        expenseListAdapter = ExpenseListAdapter(expenseList, this, this::editExpense)
        linearLayoutManager = LinearLayout(this)

    }

    private fun editExpense(id: Int){

        val intent = Intent(this, RegisterActivity::class.java)

        intent.putExtra("mode", "Edit")
        intent.putExtra("id",id)
        startActivity(intent)
    }
}
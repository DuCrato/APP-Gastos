package com.example.mypocket.dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.mypocket.model.Expense

class DataBaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$ID      INTERGER NOT NULL," +
                "$DESCRIP TEXT     NOT NULL," +
                "$VALUE   TEXT     NOT NULL," +
                "$DATE    TEXT     NOT NULL," +
                "$PARCEL  TEXT             ," +
                "$PAID    TEXT     NOT NULL," +
                "" +
                "PRIMARY KEY($ID)"            +
                ")"

        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addExpense(expense: Expense){

        val db = writableDatabase
        val valeus = contentValuesOf().apply {

            put(DESCRIP, expense.descrip)
            put(VALUE,   expense.value)
            put(DATE,    expense.date)
            put(PARCEL,  expense.parcel)
            put(PAID,    expense.paid)
        }

        db.insert(TABLE_NAME, null, valeus)
    }

    fun getExpense(id: Int): Expense{

        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()

        val expense = populateExpense(cursor)

        cursor.close()
        return expense
    }

    @SuppressLint("Range")
    fun populateExpense(cursor: Cursor): Expense{
        val expense = Expense()

        expense.id      = cursor.getInt(cursor.getColumnIndex(ID))
        expense.descrip = cursor.getString(cursor.getColumnIndex(DESCRIP))
        expense.value   = cursor.getString(cursor.getColumnIndex(VALUE))
        expense.date    = cursor.getString(cursor.getColumnIndex(DATE))
        expense.parcel  = cursor.getString(cursor.getColumnIndex(PARCEL))
        expense.paid    = cursor.getString(cursor.getColumnIndex(PAID))

        return expense
    }

    fun getExpenseList(): ArrayList<Expense>{

        val expenseList = ArrayList<Expense>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $ID"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val expense = populateExpense(cursor)
                    expenseList.add(expense)

                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return expenseList
    }

    fun updateExpense(expense: Expense){

        val db = writableDatabase
        val valeus = ContentValues().apply {

            put(DESCRIP, expense.descrip)
            put(VALUE,   expense.value)
            put(DATE,    expense.date)
            put(PARCEL,  expense.parcel)
            put(PAID,    expense.paid)
        }
        db.update(TABLE_NAME, valeus,"$ID=?", arrayOf(expense.id.toString()))
    }

    fun deleteExpense(id: Int){

        val db = writableDatabase

        db.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString()))
    }

    fun searchExpense(str: String): ArrayList<Expense>{

        val expenseList = ArrayList<Expense>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $DESCRIP LIKE '%$str%' OR $VALUE LIKE '%$str%' OR $DATE LIKE '%$str%' ORDER BY $ID"
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val expense = populateExpense(cursor)

                    expenseList.add(expense)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return expenseList
    }

    companion object{
        private val DB_VERSION = 1
        private val DB_NAME    = "SaveExpense"
        private val TABLE_NAME = "Expense"

        private val ID      = "ID"
        private val DESCRIP = "Description"
        private val VALUE   = "Value"
        private val DATE    = "Date"
        private val PARCEL  = "Parcel"
        private val PAID    = "Paid"
    }
}
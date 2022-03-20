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
                "$COLUMN_NAME_ID      INTERGER ," +
                "$COLUMN_NAME_DESCRIP TEXT     ," +
                "$COLUMN_NAME_VALUE   TEXT     ," +
                "$COLUMN_NAME_DATE    TEXT     ," +
                "$COLUMN_NAME_PARCEL  TEXT     ," +
                "$COLUMN_NAME_PAID    TEXT     ," +
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

            put(COLUMN_NAME_DESCRIP, expense.descrip)
            put(COLUMN_NAME_VALUE,   expense.value)
            put(COLUMN_NAME_DATE,    expense.date)
            put(COLUMN_NAME_PARCEL,  expense.parcel)
            put(COLUMN_NAME_PAID,    expense.paid)
        }

        db.insert(TABLE_NAME, null, valeus)
    }

    fun getExpense(id: Int): Expense{

        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME" +
                " WHERE $COLUMN_NAME_ID = $id"

        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()

        val expense = populateExpense(cursor)

        cursor.close()
        return expense
    }

    @SuppressLint("Range")
    fun populateExpense(cursor: Cursor): Expense{
        val expense = Expense()

        expense.id      = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID))
        expense.descrip = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIP))
        expense.value   = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_VALUE))
        expense.date    = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE))
        expense.parcel  = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PARCEL))
        expense.paid    = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PAID))

        return expense
    }

    fun getExpenseList(): ArrayList<Expense>{

        val expenseList = ArrayList<Expense>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME_ID"
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

            put(COLUMN_NAME_DESCRIP, expense.descrip)
            put(COLUMN_NAME_VALUE,   expense.value)
            put(COLUMN_NAME_DATE,    expense.date)
            put(COLUMN_NAME_PARCEL,  expense.parcel)
            put(COLUMN_NAME_PAID,    expense.paid)
        }
        db.update(TABLE_NAME, valeus,"$COLUMN_NAME_ID=?", arrayOf(expense.id.toString()))
    }

    fun deleteExpense(id: Int){

        val db = writableDatabase

        db.delete(TABLE_NAME, "$COLUMN_NAME_ID=?", arrayOf(id.toString()))
    }

    fun searchExpense(str: String): ArrayList<Expense>{

        val expenseList = ArrayList<Expense>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME " +
                "WHERE $COLUMN_NAME_DESCRIP LIKE '%$str%'" +
                " OR   $COLUMN_NAME_VALUE   LIKE '%$str%'" +
                " OR   $COLUMN_NAME_DATE    LIKE '%$str%'" +
                " ORDER BY $COLUMN_NAME_ID"

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
        private const val DB_VERSION = 1
        private const val DB_NAME    = "SaveExpense"
        private const val TABLE_NAME = "Expense"

        private const val COLUMN_NAME_ID      = "ID"
        private const val COLUMN_NAME_DESCRIP = "Description"
        private const val COLUMN_NAME_VALUE   = "Value"
        private const val COLUMN_NAME_DATE    = "Date"
        private const val COLUMN_NAME_PARCEL  = "Parcel"
        private const val COLUMN_NAME_PAID    = "Paid"
    }
}
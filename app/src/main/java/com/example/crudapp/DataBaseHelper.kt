package com.example.crudapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

open class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        private const val DATABASE_NAME = "database.db"
        private const val TBL_NAME = "data_table"
        private const val ID = "id"
        private const val NAME = "name"
        private const val AGE = "age"
        private const val IS_ACTIVE = "is_active"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = ("CREATE TABLE " + TBL_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, "
                + AGE + " INTEGER, " + IS_ACTIVE + " TEXT " + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_NAME")
        onCreate(db)
    }

    fun insertColumn(data: CustomerModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, data.name)
        contentValues.put(AGE, data.age)
        contentValues.put(IS_ACTIVE,data.isActive)

        val success = db.insert(TBL_NAME, null, contentValues)
        db.close()
        return success
    }

    fun getAllColumns(): ArrayList<CustomerModel> {
        val dataList: ArrayList<CustomerModel> = ArrayList()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TBL_NAME"

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var age: Int
        var isActive: String


        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                age = cursor.getInt(cursor.getColumnIndexOrThrow(AGE))
                isActive = cursor.getString(cursor.getColumnIndexOrThrow(IS_ACTIVE))


                val data = CustomerModel(id, name, age, isActive)
                dataList.add(data)
            } while (cursor.moveToNext())
        }
        return dataList
    }

    fun updateData(data: CustomerModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, data.id)
        contentValues.put(NAME, data.name)
        contentValues.put(AGE, data.age)
        contentValues.put(IS_ACTIVE,data.isActive)

        val succes = db.update(TBL_NAME, contentValues, "id=" + data.id, null)
        db.close()
        return succes
    }

    fun deleteColumnById(id: Int): Int {
        val db = this.writableDatabase

        val succes = db.delete(TBL_NAME,"id=$id",null)
        db.close()
        return succes

    }
}
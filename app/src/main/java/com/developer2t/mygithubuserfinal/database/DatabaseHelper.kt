package com.developer2t.mygithubuserfinal.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_userfav"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATED_TABLE_USERFAV =
            "CREATE TABLE ${DatabaseContract.UserColumns.TABLE_NAME} (" +
                    "${DatabaseContract.UserColumns.ID} INTEGER PRIMARY KEY," +
                    "${DatabaseContract.UserColumns.PHOTO} TEXT NOT NULL, " +
                    "${DatabaseContract.UserColumns.NAME} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATED_TABLE_USERFAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.UserColumns.TABLE_NAME}")
        onCreate(db)
    }
}
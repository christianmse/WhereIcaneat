package com.whereicaneat.domain.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.whereicaneat.common.Common
import com.whereicaneat.common.DBEntries

class FavoritosDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Favoritos.db"
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DBEntries.TABLE_NAME} (" +
                    "${DBEntries.COLUMN_NAME_UID} INTEGER PRIMARY KEY," +
                    "${DBEntries.COLUMN_NAME_NOMBRE} TEXT," +
                    "${DBEntries.COLUMN_NAME_TOKEN} TEXT, " +
                    "${DBEntries.COLUMN_NAME_FOTO} TEXT)"
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DBEntries.TABLE_NAME}"
    }
}
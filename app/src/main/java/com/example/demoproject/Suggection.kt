package com.example.demoproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.SimpleCursorAdapter


class SuggestionsDatabase(context: Context?) {
    private val db: SQLiteDatabase
    private val helper: Helper

    init {
        helper = Helper(context, DB_SUGGESTION, null, 1)
        db = helper.writableDatabase
    }

    fun insertSuggestion(text: String?): Long {
        val values = ContentValues()
        values.put(FIELD_SUGGESTION, text)
        return db.insert(TABLE_SUGGESTION, null, values)
    }

    fun getSuggestions(text: String): Cursor {
        return db.query(
            TABLE_SUGGESTION, arrayOf(FIELD_ID, FIELD_SUGGESTION),
            FIELD_SUGGESTION + " LIKE '" + text + "%'", null, null, null, null
        )
    }

    private inner class Helper(
        context: Context?, name: String?, factory: CursorFactory?,
        version: Int,
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE " + TABLE_SUGGESTION + " (" +
                        FIELD_ID + " integer primary key autoincrement, " + FIELD_SUGGESTION + " text);"
            )
            Log.d("SUGGESTION", "DB CREATED")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }

    companion object {
        const val DB_SUGGESTION = "SUGGESTION_DB"
        const val TABLE_SUGGESTION = "SUGGESTION_TB"
        const val FIELD_ID = "_id"
        const val FIELD_SUGGESTION = "suggestion"
    }
}

class SuggestionSimpleCursorAdapter : SimpleCursorAdapter {
    constructor(
        context: Context?, layout: Int, c: Cursor?,
        from: Array<String?>?, to: IntArray?,
    ) : super(context, layout, c, from, to)

    constructor(
        context: Context?, layout: Int, c: Cursor?,
        from: Array<String?>?, to: IntArray?, flags: Int,
    ) : super(context, layout, c, from, to, flags)

    override fun convertToString(cursor: Cursor): CharSequence {
        val indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.FIELD_SUGGESTION)
        return cursor.getString(indexColumnSuggestion)
    }
}
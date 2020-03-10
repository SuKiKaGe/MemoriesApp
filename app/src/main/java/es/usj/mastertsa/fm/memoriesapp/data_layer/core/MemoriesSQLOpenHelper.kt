package es.usj.mastertsa.fm.memoriesapp.data_layer.core

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.sqlite.*

class MemoriesSQLOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private val CREATE_MEMORIES_TABLE =
        "CREATE TABLE $MEMORIES_TABLE ($ID INTEGER PRIMARY KEY, $TITLE TEXT, $CATEGORY TEXT, $DESCRIPTION TEXT, $LATITUDE REAL, $LONGITUDE REAL, $DATE TEXT, $PHOTOPATH TEXT, $VIDEOPATH TEXT, $AUDIOPATH TEXT)"
    private val DROP_MEMORIES_TABLE =
        "DROP TABLE IF EXISTS $MEMORIES_TABLE"

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_MEMORIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db!!.execSQL(DROP_MEMORIES_TABLE)
            db.execSQL(CREATE_MEMORIES_TABLE)
        }
    }

}
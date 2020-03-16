package es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.MemoriesSQLOpenHelper
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val MEMORIES_TABLE = "memories"
const val ID = "rowid"
const val TITLE = "title"
const val CATEGORY = "category"
const val DESCRIPTION = "description"
const val LATITUDE = "latitude"
const val LONGITUDE = "longitude"
const val DATE = "date"
const val PHOTOPATH = "photoPath"
const val VIDEOPATH = "videoPath"
const val AUDIOPATH = "audioPath"

class MemorySQLiteDao(context : Context) : IDao<MemoryModel>
{
    private var databaseHelper : MemoriesSQLOpenHelper

    init
    {
        val helper =
            MemoriesSQLOpenHelper(
                context,
                "MemoriesDB",
                null,
                3
            )

        databaseHelper = helper
    }

    private val select = "SELECT $ID, $TITLE, $CATEGORY, $DESCRIPTION, $LATITUDE, $LONGITUDE, $DATE, $PHOTOPATH, $VIDEOPATH, $AUDIOPATH FROM $MEMORIES_TABLE"

    override fun insert(element : MemoryModel) : MemoryModel
    {
        val id : Long = databaseHelper.writableDatabase
            .insert(MEMORIES_TABLE, "", element.toContentValues())

        element.id = id

        return element
    }

    override fun update(element : MemoryModel) : Long?
    {
        return databaseHelper.writableDatabase.update(
            MEMORIES_TABLE,
            element.toContentValues(),
            "$ID = ?",
            arrayOf(element.id.toString())
        ).toLong()
    }

    override fun delete(id : Long) : MemoryModel?
    {
        val element : MemoryModel? = findById(id)

        if(element != null)
            databaseHelper.writableDatabase.delete(MEMORIES_TABLE, "$ID = $id", null)

        return element
    }

    @SuppressLint("Recycle")
    override fun list() : List<MemoryModel>
    {
        val result : ArrayList<MemoryModel> = arrayListOf()
        val cursor : Cursor = databaseHelper.readableDatabase.
            rawQuery(select, null)

        if(cursor.moveToFirst())
        {
            do {
                val memory : MemoryModel? = cursor.getMemory()

                result.add(memory!!)
            } while (cursor.moveToNext())
        }

        return result
    }

    override fun findById(id : Long) : MemoryModel?
    {
        var memory : MemoryModel? = null
        val cursor : Cursor = databaseHelper.readableDatabase.
            query(
                table = MEMORIES_TABLE,
                columns = arrayOf(
                    ID,
                    TITLE,
                    CATEGORY,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    DATE,
                    PHOTOPATH,
                    VIDEOPATH,
                    AUDIOPATH
                ),
                whereClause = "$ID = ?",
                whereArgs = arrayOf("$id"))

        if(cursor.moveToFirst())
        {
            memory = cursor.getMemory()
        }

        return memory
    }
}

private fun Cursor.getMemory() : MemoryModel?
{
    val id : Long = getLong(0)
    val title : String = getString(1)
    val category : String = getString(2)
    val description : String = getString(3)
    val latitude : Float = getFloat(4)
    val longitude : Float = getFloat(5)
    val date : Date = getString(6).toDate()
    val photoPath : String? = getString(7)
    val videoPath : String? = getString(8)
    val audioPath : String? = getString(9)

    return MemoryModel(
        id,
        title,
        category,
        description,
        latitude,
        longitude,
        date,
        photoPath,
        videoPath,
        audioPath
    )
}

@SuppressLint("SimpleDateFormat")
private fun String.toDate() : Date
{
    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")

    return formatter.parse(this)!!
}

private fun SQLiteDatabase.query(table : String, columns : Array<String>, whereClause : String,
                                 whereArgs : Array<String>) : Cursor
{
    return query(table, columns, whereClause, whereArgs, null, null, null)
}

private fun MemoryModel.toContentValues() : ContentValues = ContentValues().apply {
    if(id != 0L) put(ID, id)
    put(TITLE, title)
    put(CATEGORY, category)
    put(DESCRIPTION, description)
    put(LATITUDE, latitude)
    put(LONGITUDE, longitude)
    put(DATE, date.convertToIso())
    put(PHOTOPATH, photoPath)
    put(VIDEOPATH, videoPath)
    put(AUDIOPATH, audioPath)
}

@SuppressLint("SimpleDateFormat")
private fun Date.convertToIso() : String?
{
    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")

    return formatter.format(this)
}

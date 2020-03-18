package es.usj.mastertsa.fm.memoriesapp.data_layer.core

import android.content.Context
import androidx.room.*
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.room.MemoryIDAO
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel
import java.util.*

@Database(entities = [MemoryModel::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun memoryModelDao() : MemoryIDAO

    companion object
    {
        private var INSTANCE : AppDatabase? = null

        fun getAppDataBase(context : Context) : AppDatabase?
        {
            if (INSTANCE == null)
            {
                synchronized(AppDatabase::class)
                {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        "memories.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        @Suppress("unused")
        fun destroyDataBase()
        {
            INSTANCE = null
        }
    }
}

class DateTypeConverter
{
    @TypeConverter
    fun fromTimestamp(value : Long?) : Date?
    {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date : Date?) : Long?
    {
        return date?.time
    }
}

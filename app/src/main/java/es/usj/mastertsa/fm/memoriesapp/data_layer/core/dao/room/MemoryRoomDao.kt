@file:Suppress("AndroidUnresolvedRoomSqlReference")

package es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.room

import android.content.Context
import androidx.room.*
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.AppDatabase
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

@Dao
interface MemoryIDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memory : MemoryModel)

    @Query("DELETE FROM memories")
    fun deleteAll()

    @Delete
    fun delete(memory : MemoryModel?)

    @Query("SELECT * from memories WHERE id == :id")
    fun getMemory(id : Long) : MemoryModel

    @Query("SELECT * from memories ORDER BY id ASC")
    fun getAllMemories() : List<MemoryModel>

    @Update
    fun update(memory : MemoryModel)
}

class MemoryRoomDao(context : Context? = null) : IDao<MemoryModel>
{
    private var db : AppDatabase? = null
    private var memoryIDAO : MemoryIDAO? = null

    init
    {
        db = AppDatabase.getAppDataBase(context = context!!)
        memoryIDAO = db?.memoryModelDao()
    }

    override fun insert(element : MemoryModel) : MemoryModel
    {
        memoryIDAO!!.insert(element)

        return findById(element.id)!!
    }

    override fun update(element : MemoryModel) : Long?
    {
        memoryIDAO!!.update(element)

        return element.id
    }

    override fun delete(id : Long) : MemoryModel?
    {
        val memory : MemoryModel? = findById(id)

        memoryIDAO!!.delete(memory)

        return memory
    }

    override fun list() : List<MemoryModel>
    {
        return memoryIDAO!!.getAllMemories()
    }

    override fun findById(id : Long) : MemoryModel?
    {
        return memoryIDAO!!.getMemory(id)
    }
}

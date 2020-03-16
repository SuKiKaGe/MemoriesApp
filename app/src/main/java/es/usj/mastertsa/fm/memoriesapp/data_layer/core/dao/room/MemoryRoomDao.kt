@file:Suppress("AndroidUnresolvedRoomSqlReference")

package es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.room

import androidx.room.*
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

@Dao
interface MemoryIDAO
{
    @Insert
    fun insert(memory : MemoryModel) : MemoryModel

    @Query("DELETE FROM memories")
    fun deleteAll()

    @Delete
    fun delete(memory : MemoryModel?)

    @Query("SELECT * from memories ORDER BY id ASC")
    fun getAllMemories()

    @Update
    fun update(memory : MemoryModel)
}

class MemoryRoomDao : IDao<MemoryModel>
{
    private val memoryIDAO : MemoryIDAO? = null

    override fun insert(element : MemoryModel) : MemoryModel
    {
        return memoryIDAO!!.insert(element)
    }

    override fun update(element : MemoryModel) : Long?
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id : Long) : MemoryModel?
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list() : List<MemoryModel>
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id : Long) : MemoryModel?
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

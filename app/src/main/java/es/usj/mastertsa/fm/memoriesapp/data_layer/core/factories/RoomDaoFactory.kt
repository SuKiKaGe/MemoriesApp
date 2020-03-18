package es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories

import android.content.Context
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.DaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.room.MemoryRoomDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

class RoomDaoFactory : DaoFactory()
{
    override fun getMemoryDAO(context : Context) : IDao<MemoryModel>
    {
        return MemoryRoomDao(context)
    }
}

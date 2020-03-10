package es.usj.mastertsa.fm.memoriesapp.data_layer.core

import android.content.Context
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.FirebaseDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.RoomDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.SQLDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

val DATASOURCE =
    DatasourceType.SQLITE

enum class DatasourceType { SQLITE, FIREBASE}

abstract class DaoFactory {

    companion object
    {
        fun getFactory() : DaoFactory
        {
            if(DATASOURCE == DatasourceType.SQLITE)
            {
                return SQLDaoFactory()
            } else if (DATASOURCE == DatasourceType.FIREBASE)
            {
                return FirebaseDaoFactory()
            }
            else
            {
                return RoomDaoFactory()
            }
        }
    }

    abstract fun getMemoryDAO (context : Context) : IDao<MemoryModel>
}
package es.usj.mastertsa.fm.memoriesapp.data_layer.core

import android.content.Context
import android.util.Log
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.FirebaseDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.RoomDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.SQLDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

val DATASOURCE = DatasourceType.ROOM

enum class DatasourceType { SQLITE, FIREBASE, ROOM}

abstract class DaoFactory
{
    companion object
    {
        fun getFactory() : DaoFactory
        {
            Log.v("DATASOURCE", DATASOURCE.toString())

            return when (DATASOURCE)
            {
                DatasourceType.SQLITE -> {
                    SQLDaoFactory()
                }
                DatasourceType.FIREBASE -> {
                    FirebaseDaoFactory()
                }
                else -> {
                    RoomDaoFactory()
                }
            }
        }
    }

    abstract fun getMemoryDAO (context : Context) : IDao<MemoryModel>
}

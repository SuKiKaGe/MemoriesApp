package es.usj.mastertsa.fm.memoriesapp.data_layer.core

import android.content.Context
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.FirebaseDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.RoomDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories.SQLDaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

val DATASOURCE = DatasourceType.SQLITE

enum class DatasourceType { SQLITE, FIREBASE}

abstract class DaoFactory
{
    companion object
    {
        fun getFactory() : DaoFactory
        {
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

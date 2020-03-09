package es.usj.mastertsa.fm.memoriesapp.DataLayer.core.factories

import android.content.Context
import es.usj.mastertsa.fm.memoriesapp.DataLayer.core.DaoFactory
import es.usj.mastertsa.fm.memoriesapp.DataLayer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.DataLayer.core.dao.sqlite.MemorySQLiteDao
import es.usj.mastertsa.fm.memoriesapp.DataLayer.model.MemoryModel

class SQLDaoFactory : DaoFactory()
{
    override fun getMemoryDAO(context : Context): IDao<MemoryModel> {
        return MemorySQLiteDao(
            context
        )
    }
}
package es.usj.mastertsa.fm.memoriesapp.data_layer.core.factories

import android.content.Context
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.DaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.firebase.MemoryFirebaseDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

class FirebaseDaoFactory : DaoFactory()
{
    override fun getMemoryDAO(context : Context) : IDao<MemoryModel>
    {
        return MemoryFirebaseDao()
    }
}

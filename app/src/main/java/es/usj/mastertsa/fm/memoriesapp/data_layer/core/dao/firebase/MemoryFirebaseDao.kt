package es.usj.mastertsa.fm.memoriesapp.data_layer.core.dao.firebase

import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao

class MemoryFirebaseDao :
    IDao<MemoryModel> {
    override fun insert(element: MemoryModel): MemoryModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(element: MemoryModel): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Long): MemoryModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): List<MemoryModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): MemoryModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
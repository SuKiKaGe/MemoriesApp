package es.usj.mastertsa.fm.memoriesapp

import android.content.Context

class MemoriesManager private constructor() {
    private object HOLDER {
        val INSTANCE = MemoriesManager()
    }

    companion object {
        val instance: MemoriesManager by lazy { HOLDER.INSTANCE }
    }

    var memories: ArrayList<Memory> = ArrayList()
    var maxId: Int = 1

    fun addMemorie(newMemorie: Memory)
    {
        newMemorie.id = maxId
        maxId++

        memories.add(newMemorie)
    }

    fun deleteMemorie(id: Int)
    {
        var memorieToDelete = memories.find { it.id == id }

        memories.remove(memorieToDelete)
    }

    fun updateMemorie(memorie: Memory)
    {
        memories.set(memorie.id, memorie)
    }
}
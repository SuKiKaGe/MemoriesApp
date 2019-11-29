package es.usj.mastertsa.fm.memoriesapp

class MemoriesManager private constructor()
{
    private object HOLDER
    {
        val INSTANCE = MemoriesManager()
    }

    companion object
    {
        val instance: MemoriesManager by lazy { HOLDER.INSTANCE }
    }

    var memories: ArrayList<Memory> = ArrayList()
    var maxId: Int = 1

    fun addMemory(newMemory: Memory)
    {
        newMemory.id = maxId
        maxId++

        memories.add(newMemory)
    }

    fun deleteMemory(id: Int)
    {
        var memoryToDelete = memories.find { it.id == id }

        memories.remove(memoryToDelete)
    }

    fun updateMemory(memory: Memory)
    {
        memories[memory.id] = memory
    }
}
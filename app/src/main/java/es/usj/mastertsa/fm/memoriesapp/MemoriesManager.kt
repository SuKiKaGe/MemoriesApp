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
        val position = memories.indexOf(memories.find { it.id == memory.id })
        memories[position] = memory
    }

    fun getMemories(order: Order = Order.Date): ArrayList<Memory>
    {
        var ordedMemories: List<Memory> = when(order)
        {
            Order.Date -> memories.sortedBy { it.date }
            Order.Name_A_Z -> memories.sortedBy { it.title }
            Order.Name_Z_A ->
            {
                val temp = memories.sortedBy { it.title }
                temp.reversed()
            }
            Order.Category_A_Z -> {memories.sortedBy { it.category }}
            Order.Category_Z_A ->
            {
                val temp = memories.sortedBy { it.category }
                temp.reversed()
            }
        }

        return ArrayList(ordedMemories)
    }
}
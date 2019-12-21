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
    lateinit var tempMemory: Memory
    var orderSelected: Order = Order.Date

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

    fun findMemory(id: Int): Memory?
    {
        if(id == 0) return tempMemory
        return memories.find { it.id == id }
    }

    fun getMemories(order: Order?): ArrayList<Memory>
    {
        if(order != null) orderSelected = order

        var ordedMemories: List<Memory> = when(orderSelected)
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
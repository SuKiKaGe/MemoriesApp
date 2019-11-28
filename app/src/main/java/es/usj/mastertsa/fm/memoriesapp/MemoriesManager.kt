package es.usj.mastertsa.fm.memoriesapp

class MemoriesManager {

    private lateinit var instance: MemoriesManager

    lateinit var memories: ArrayList<Memorie>
    var maxId: Int = 1

    fun getInstance(): MemoriesManager
    {
        if(instance == null)
        {
            instance = MemoriesManager()
        }

        return instance
    }

    fun addMemorie(newMemorie: Memorie)
    {
        newMemorie.id = maxId
        maxId++

        memories.add(newMemorie)
    }

    fun deleteMemorie(id: Int)
    {

    }
}
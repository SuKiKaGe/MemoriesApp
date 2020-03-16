package es.usj.mastertsa.fm.memoriesapp.domain_layer

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.DaoFactory
import es.usj.mastertsa.fm.memoriesapp.data_layer.core.IDao
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel
import es.usj.mastertsa.fm.memoriesapp.presentation_layer.Order

class MemoriesManager private constructor()
{
    //region Singleton
    private object HOLDER
    {
        val INSTANCE = MemoriesManager()
    }

    companion object
    {
        val instance : MemoriesManager by lazy { HOLDER.INSTANCE }
    }
    //endregion

    private lateinit var dao : IDao<MemoryModel>
    lateinit var tempMemory : Memory
    var orderSelected : Order = Order.Date

    fun init(context : Context)
    {
        dao = DaoFactory.getFactory().getMemoryDAO(context)

        if (dao.list().isEmpty()) { fillMemoriesWithExamples() }
    }

    fun addMemory(newMemory : Memory)
    {
        val memoryModel : MemoryModel = MemoryAdapter.getMemoryModelFromMemory(newMemory)

        dao.insert(memoryModel)
    }

    fun deleteMemory(id : Int)
    {
        dao.delete(id.toLong())
    }

    fun updateMemory(memory : Memory)
    {
        val memoryModel : MemoryModel = MemoryAdapter.getMemoryModelFromMemory(memory)

        dao.update(memoryModel)
    }

    fun findMemory(id : Int) : Memory?
    {
        if (id == 0)
        {
            return tempMemory
        }
        else
        {
            val memoryModel : MemoryModel? = dao.findById(id.toLong())

            if (memoryModel != null)
            {
                return MemoryAdapter.getMemoryFromMemoryModel(memoryModel)
            }
        }

        return null
    }

    fun getMemories(order : Order?) : ArrayList<Memory>
    {
        val memoriesModel : List<MemoryModel> = dao.list()

        val memories : ArrayList<Memory> = ArrayList()

        memoriesModel.forEach {
            memories.add(MemoryAdapter.getMemoryFromMemoryModel(it))
        }

        if (order != null) orderSelected = order

        val orderMemories : List<Memory> = when(orderSelected)
        {
            Order.Date -> memories.sortedBy { it.date }
            Order.Name_A_Z -> memories.sortedBy { it.title }
            Order.Name_Z_A ->
            {
                val temp : List<Memory> = memories.sortedBy { it.title }
                temp.reversed()
            }
            Order.Category_A_Z -> { memories.sortedBy { it.category } }
            Order.Category_Z_A ->
            {
                val temp : List<Memory> = memories.sortedBy { it.category }
                temp.reversed()
            }
        }

        return ArrayList(orderMemories)
    }

    private fun fillMemoriesWithExamples()
    {
        addMemory(
            Memory(
                1,
                "Perdí 20 euros", "Bad",
                "Estaba caminando por la calle y me di cuenta que en algún momento" +
                        "sacando el móvil se me calló un billete que llevaba en el bolsillo",
                LatLng(41.671128, -0.889325)
            )
        )

        addMemory(
            Memory(
                2,
                "Mi pareja me dejó",
                "BlackList",
                "Celebrando nuestro tercer aniversario decidió que no quería seguir" +
                        "conmigo",
                LatLng(41.667598, -0.892878)
            )
        )

        instance.addMemory(
            Memory(
                3,
                "El Pilar", "Travel",
                "Gran viaje a una ciudad preciosa y fría",
                LatLng(41.656565, -0.878166)
            )
        )

        instance.addMemory(
            Memory(
                4,
                "Magia potagia",
                "Friends",
                "Tarde en el teatro central viendo un espectáculo de magia con buena" +
                        "compañía",
                LatLng(41.651768, -0.879397)
            )
        )

        instance.addMemory(
            Memory(
                5,
                "Patrón USJ", "Celebration",
                "Celebración del patrón de la universidad",
                LatLng(41.756797, -0.832194)
            )
        )

        instance.addMemory(
            Memory(
                6,
                "Bienvenido a mi vida", "Love",
                "Hemos adoptado a nuestro bebé, bienvenido Pancho",
                LatLng(41.767716, -0.825706)
            )
        )
    }
}

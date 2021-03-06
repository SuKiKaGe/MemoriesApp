package es.usj.mastertsa.fm.memoriesapp.domain_layer

import com.google.android.gms.maps.model.LatLng
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel

object MemoryAdapter
{
    fun getMemoryFromMemoryModel(memoryModel : MemoryModel) : Memory
    {
        val latLng = LatLng(memoryModel.latitude.toDouble(), memoryModel.longitude.toDouble())

        return Memory(memoryModel.id.toInt(), memoryModel.title, memoryModel.category,
            memoryModel.description, latLng, memoryModel.date, memoryModel.photoPath,
            memoryModel.videoPath, memoryModel.audioPath)
    }

    fun getMemoryModelFromMemory(memory : Memory) : MemoryModel
    {
        return MemoryModel(memory.id.toLong(), memory.title, memory.category.toString(),
            memory.description, memory.location.latitude.toFloat(),
            memory.location.longitude.toFloat(), memory.date, memory.photoPath, memory.videoPath,
            memory.audioPath)
    }
}

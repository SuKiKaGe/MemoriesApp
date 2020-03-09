package es.usj.mastertsa.fm.memoriesapp.DataLayer.model

import java.util.*

enum class Categories { Bad, BlackList, Celebration, Friends, Good, Love, Travel }

data class MemoryModel(var id : Long,
                       val title : String,
                       val category : String,
                       val description : String,
                       val latitude : Float,
                       val longitude : Float,
                       val date : Date,
                       val photoPath : String?,
                       val videoPath : String?,
                       val audioPath : String?)
{
    override fun toString() : String
    {
        return "$id.- $title"
    }
}
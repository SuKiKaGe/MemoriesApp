package es.usj.mastertsa.fm.memoriesapp.data_layer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

enum class Categories { Bad, BlackList, Celebration, Friends, Good, Love, Travel }

@Entity(tableName = "memories")
data class MemoryModel(
    @PrimaryKey(autoGenerate = true)
    var id : Long,
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

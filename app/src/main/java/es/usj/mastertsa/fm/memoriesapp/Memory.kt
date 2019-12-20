package es.usj.mastertsa.fm.memoriesapp

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

enum class Categories { Good, Bad, Celebration, BlackList, Love, Friends, Travel }

class Memory
{
    var id : Int = 0
    var title : String
    var category : Categories
    var date : Date
    var description : String

    var photoPath : String?
    var videoPath : String?
    var audioPath : String?

    var location : LatLng

    constructor (title : String, category : String, description : String, location: LatLng, photoPath : String? = null, date : Date = Date())
    {
        this.title = title
        this.category = Categories.valueOf(category)
        this.date = date
        this.description = description
        this.location = location
        this.photoPath = photoPath
        this.videoPath = null
        this.audioPath = null
    }
}
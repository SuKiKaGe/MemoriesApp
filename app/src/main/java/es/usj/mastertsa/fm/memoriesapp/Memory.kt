package es.usj.mastertsa.fm.memoriesapp

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.util.*

enum class Categories { Bad, BlackList, Celebration, Friends, Good, Love, Travel }

class Memory(
    var title: String,
    category: String,
    var description: String,
    var location: LatLng,
    var date: Date = Date(),
    var photoPath: String? = null,
    var videoPath: String? = null,
    var audioPath: String? = null
) {
    var id : Int = 0
    var category : Categories

    init {
        this.category = Categories.valueOf(category)
    }

    fun getColor(): Float
    {
        return when(category)
        {
            Categories.Good -> BitmapDescriptorFactory.HUE_AZURE
            Categories.Bad -> BitmapDescriptorFactory.HUE_VIOLET
            Categories.Celebration -> BitmapDescriptorFactory.HUE_BLUE
            Categories.BlackList -> BitmapDescriptorFactory.HUE_RED
            Categories.Love -> BitmapDescriptorFactory.HUE_ROSE
            Categories.Friends -> BitmapDescriptorFactory.HUE_YELLOW
            Categories.Travel -> BitmapDescriptorFactory.HUE_GREEN
        }
    }
}

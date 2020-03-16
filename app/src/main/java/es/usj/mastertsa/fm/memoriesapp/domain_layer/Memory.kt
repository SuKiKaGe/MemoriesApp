package es.usj.mastertsa.fm.memoriesapp.domain_layer

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.util.*

enum class Categories
{
    Bad { override fun getURL() : String = BAD_IMAGE_URL },
    BlackList { override fun getURL() : String = BLACKLIST_IMAGE_URL },
    Celebration { override fun getURL() : String = CELEBRATION_IMAGE_URL },
    Friends { override fun getURL() : String = FRIENDS_IMAGE_URL },
    Good { override fun getURL() : String = GOOD_IMAGE_URL },
    Love { override fun getURL() : String = LOVE_IMAGE_URL },
    Travel { override fun getURL() : String = TRAVEL_IMAGE_URL };

    abstract fun getURL() : String
}

class Memory(var id : Int = 0,
             var title : String,
             category : String,
             var description : String,
             var location : LatLng,
             var date : Date = Date(),
             var photoPath : String? = null,
             var videoPath : String? = null,
             var audioPath : String? = null)
{
    var category : Categories = Categories.valueOf(category)

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

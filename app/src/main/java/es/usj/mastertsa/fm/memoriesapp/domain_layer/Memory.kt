package es.usj.mastertsa.fm.memoriesapp.domain_layer

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.util.*

enum class Categories {
    Bad {
        override fun getURL()= "https://i.imgur.com/XCu3K9s.png"
    },
    BlackList {
        override fun getURL()= "https://i.imgur.com/1LNLyZN.png"
    },
    Celebration {
        override fun getURL()= "https://i.imgur.com/6NyvhjJ.png"
    },
    Friends {
        override fun getURL()= "https://i.imgur.com/ksLgsHG.png"
    },
    Good {
        override fun getURL()= "https://i.imgur.com/nRCzFL5.png"
    },
    Love {
        override fun getURL()= "https://i.imgur.com/LfMmskk.png"
    },
    Travel {
        override fun getURL()= "https://i.imgur.com/7b6L93P.png"
    };

    abstract fun getURL() : String
}

class Memory(
    var id : Int = 0,
    var title: String,
    category : String,
    var description: String,
    var location: LatLng,
    var date: Date = Date(),
    var photoPath: String? = null,
    var videoPath: String? = null,
    var audioPath: String? = null
) {

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

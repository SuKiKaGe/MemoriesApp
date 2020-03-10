package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.squareup.picasso.Picasso

enum class ImageType
{
    audio_available,
    audio_not_available,
    audio_playing,
    photo_available,
    photo_not_available,
    video_available,
    video_not_available
}

object ImageManager
{
    private lateinit var audio_available : Bitmap
    private lateinit var audio_not_available : Bitmap
    private lateinit var audio_playing : Bitmap
    private lateinit var photo_available : Bitmap
    private lateinit var photo_not_available : Bitmap
    private lateinit var video_available : Bitmap
    private lateinit var video_not_available : Bitmap

    init
    {
        Log.v("INIT", "Image Manager Initialized.")
    }

    fun hoardRequiredImages()
    {
        Log.v("HOARD", "Hoarding Images Started.")

        getBitmap("https://i.imgur.com/LatmDq4.png", ImageType.audio_available)
        getBitmap("https://i.imgur.com/cRM3Eu6.png", ImageType.audio_not_available)
        getBitmap("https://i.imgur.com/5of2CCk.png", ImageType.audio_playing)
        getBitmap("https://i.imgur.com/zZeuRW5.png", ImageType.photo_available)
        getBitmap("https://i.imgur.com/k5iN6te.png", ImageType.photo_not_available)
        getBitmap("https://i.imgur.com/S27FYqj.png", ImageType.video_available)
        getBitmap("https://i.imgur.com/vE3M20t.png", ImageType.video_not_available)
    }

    private fun getBitmap(url : String, imageType: ImageType)
    {
        Picasso.get()
            .load(url)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    //To change body of created functions use File | Settings | File Templates.
                    Log.v("Error", e?.localizedMessage!!)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    //To change body of created functions use File | Settings | File Templates.
                    Log.v("IMAGE", "$imageType Image Prepared.")
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                    Log.v("IMAGE", "$imageType Image Successfully Hoarded!")

                    when(imageType)
                    {
                        ImageType.audio_available -> audio_available = bitmap!!
                        ImageType.audio_not_available -> audio_not_available = bitmap!!
                        ImageType.audio_playing -> audio_playing = bitmap!!
                        ImageType.photo_available -> photo_available = bitmap!!
                        ImageType.photo_not_available -> photo_not_available = bitmap!!
                        ImageType.video_available -> video_available = bitmap!!
                        ImageType.video_not_available -> video_not_available = bitmap!!
                    }
                }
            })
    }

    fun getImage(imageType: ImageType) : Bitmap
    {
        return when(imageType) {
            ImageType.audio_available -> audio_available
            ImageType.audio_not_available -> audio_not_available
            ImageType.audio_playing -> audio_available
            ImageType.photo_available -> photo_available
            ImageType.photo_not_available -> photo_not_available
            ImageType.video_available -> video_available
            ImageType.video_not_available -> video_not_available
        }
    }
}

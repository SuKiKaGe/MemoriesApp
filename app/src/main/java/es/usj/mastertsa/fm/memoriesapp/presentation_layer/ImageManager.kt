package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import es.usj.mastertsa.fm.memoriesapp.R
import es.usj.mastertsa.fm.memoriesapp.domain_layer.*

@Suppress("EnumEntryName")
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
    private lateinit var appContext : Context
    private lateinit var audio_available : Bitmap
    private lateinit var audio_not_available : Bitmap
    private lateinit var audio_playing : Bitmap
    private lateinit var photo_available : Bitmap
    private lateinit var photo_not_available : Bitmap
    private lateinit var video_available : Bitmap
    private lateinit var video_not_available : Bitmap

    init { Log.v("INIT", "Image Manager Initialized.") }

    fun setContext(context : Context) { appContext = context }

    fun hoardRequiredImages()
    {
        Log.v("HOARD", "Hoarding Images Started.")

        getBitmap(AUDIO_AVAILABLE_IMAGE_URL, ImageType.audio_available)
        getBitmap(AUDIO_NOT_AVAILABLE_IMAGE_URL, ImageType.audio_not_available)
        getBitmap(AUDIO_PLAYING_IMAGE_URL, ImageType.audio_playing)
        getBitmap(PHOTO_AVAILABLE_IMAGE_URL, ImageType.photo_available)
        getBitmap(PHOTO_NOT_AVAILABLE_IMAGE_URL, ImageType.photo_not_available)
        getBitmap(VIDEO_AVAILABLE_IMAGE_URL, ImageType.video_available)
        getBitmap(VIDEO_NOT_AVAILABLE_IMAGE_URL, ImageType.video_not_available)
    }

    private fun getBitmap(url : String, imageType : ImageType)
    {
        Picasso.get()
            .load(url)
            .into(object : com.squareup.picasso.Target
            {
                override fun onBitmapFailed(e : java.lang.Exception?, errorDrawable : Drawable?)
                {
                    //To change body of created functions use File | Settings | File Templates.
                    Log.v("PICASSO", e?.localizedMessage!!)
                }

                override fun onPrepareLoad(placeHolderDrawable : Drawable?)
                {
                    //To change body of created functions use File | Settings | File Templates.
                    Log.v("PICASSO", "$imageType Image Prepared.")
                }

                override fun onBitmapLoaded(bitmap : Bitmap?, from : Picasso.LoadedFrom?)
                {
                    Log.v("PICASSO", "$imageType Image Successfully Hoarded!")

                    when(imageType)
                    {
                        ImageType.audio_available -> ::audio_available.set(bitmap!!)
                        ImageType.audio_not_available -> ::audio_not_available.set(bitmap!!)
                        ImageType.audio_playing -> ::audio_playing.set(bitmap!!)
                        ImageType.photo_available -> ::photo_available.set(bitmap!!)
                        ImageType.photo_not_available -> ::photo_not_available.set(bitmap!!)
                        ImageType.video_available -> ::video_available.set(bitmap!!)
                        ImageType.video_not_available -> ::video_not_available.set(bitmap!!)
                    }
                }
            })
    }

    fun getImage(imageType : ImageType) : Bitmap
    {
        when(imageType)
        {
            ImageType.audio_available -> {
                return if (::audio_available.isInitialized) { audio_available }
                else { getImageNotAvailableIcon() }
            }
            ImageType.audio_not_available -> {
                return if (::audio_not_available.isInitialized) { audio_not_available }
                else { getImageNotAvailableIcon() }
            }
            ImageType.audio_playing -> {
                return if (::audio_playing.isInitialized) { audio_playing }
                else { getImageNotAvailableIcon() }
            }
            ImageType.photo_available -> {
                return if (::photo_available.isInitialized) { photo_available }
                else { getImageNotAvailableIcon() }
            }
            ImageType.photo_not_available -> {
                return if (::photo_not_available.isInitialized) { photo_not_available }
                else { getImageNotAvailableIcon() }
            }
            ImageType.video_available -> {
                return if (::video_available.isInitialized) { video_available }
                else { getImageNotAvailableIcon() }
            }
            ImageType.video_not_available -> {
                return if (::video_not_available.isInitialized) { video_not_available }
                else { getImageNotAvailableIcon() }
            }
        }
    }

    private fun getImageNotAvailableIcon() : Bitmap
    {
        val drawableImageNotAvailableIcon : Drawable? =
            ResourcesCompat.getDrawable(
                appContext.resources,
                R.drawable.no_image_available, null)

        return drawableImageNotAvailableIcon!!.toBitmap()
    }
}

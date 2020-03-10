package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.usj.mastertsa.fm.memoriesapp.domain_layer.*
import es.usj.mastertsa.fm.memoriesapp.R
import kotlinx.android.synthetic.main.activity_view_memory.*
import kotlinx.android.synthetic.main.activity_view_memory.imgBtnAudio
import kotlinx.android.synthetic.main.activity_view_memory.imgBtnPhoto
import kotlinx.android.synthetic.main.activity_view_memory.imgBtnVideo
import java.text.SimpleDateFormat

class ViewMemory : AppCompatActivity(), MapFragment.MapInterface
{
    var id : Int = 0
    private lateinit var location : LatLng
    var color: Float = 0f

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory)

        if(id == 0) id = intent.getIntExtra(ID, 0)

        setViewContent()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setViewContent()
    {
        //val memory = MemoriesManager.instance.memories.find { it.id == id }!!

        val memory = MemoriesManager.instance.findMemory(id)!!

        title = memory.title
        tvCategoryItem.text = memory.category.toString()
        tvDateItem.text = (SimpleDateFormat("dd/MM/yyyy").format(memory.date))
        tvDescription.text = memory.description

        if (!memory.photoPath.isNullOrEmpty())
        {
            //imgBtnPhoto.setImageResource(R.drawable.photo_available)
            imgBtnPhoto.setImageBitmap(ImageManager.getImage(ImageType.photo_available))
            imgBtnPhoto.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, memory.photoPath)
                intent.putExtra(
                    MULTIMEDIA,
                    MULTIMEDIA_PHOTO
                )
                startActivityForResult(intent,
                    VIEW_MEMORY_PHOTO
                )
            }
        }
        else
        {
            imgBtnPhoto.setImageBitmap(ImageManager.getImage(ImageType.photo_not_available))
        }

        if (!memory.videoPath.isNullOrEmpty())
        {
            //imgBtnVideo.setImageResource(R.drawable.video_available)
            imgBtnVideo.setImageBitmap(ImageManager.getImage(ImageType.video_available))
            imgBtnVideo.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, memory.videoPath)
                intent.putExtra(
                    MULTIMEDIA,
                    MULTIMEDIA_VIDEO
                )
                startActivityForResult(intent,
                    VIEW_MEMORY_PHOTO
                )
            }
        }
        else
        {
            imgBtnVideo.setImageBitmap(ImageManager.getImage(ImageType.video_not_available))
        }

        if (!memory.audioPath.isNullOrEmpty())
        {
            //imgBtnAudio.setImageResource(R.drawable.audio_available)
            imgBtnAudio.setImageBitmap(ImageManager.getImage(ImageType.audio_available))
            imgBtnAudio.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, memory.audioPath)
                intent.putExtra(
                    MULTIMEDIA,
                    MULTIMEDIA_AUDIO
                )
                startActivityForResult(intent,
                    VIEW_MEMORY_PHOTO
                )
            }
        }
        else
        {
            imgBtnAudio.setImageBitmap(ImageManager.getImage(ImageType.audio_not_available))
        }

        location = memory.location
        color = memory.getColor()
    }

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.options_view, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item : MenuItem?) : Boolean
    {
        when(item!!.itemId)
        {
            R.id.edit -> {
                val intent = Intent(this, ManageMemory::class.java)
                intent.putExtra(ACTION, "Edit Memory")
                intent.putExtra(ID, id)
                startActivityForResult(intent,
                    UPDATE_MEMORY
                )
            }

            R.id.delete -> {
                MemoriesManager.instance.deleteMemory(id)
                setResult(DELETE_MEMORY)
                finish()
            }

            else -> Toast.makeText(this, "Something was wrong.",
                Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode)
        {
            UPDATE_MEMORY -> setViewContent()
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        setResult(UNMODIFIED)
    }

    override fun locateMap(map : GoogleMap?)
    {
        map?.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(color)))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }
}

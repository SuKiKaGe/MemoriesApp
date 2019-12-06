package es.usj.mastertsa.fm.memoriesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_memory.*
import java.text.SimpleDateFormat
import java.util.*


const val ACTION = "New/Edit Memory"
const val ID = "Memory Id"
const val REQUEST_IMAGE_CAPTURE = 1
const val REQUEST_VIDEO_CAPTURE = 2
const val REQUEST_AUDIO_CAPTURE = 3

class ManageMemory : AppCompatActivity(), LocationListener
{
    var idToEdit: Int = 0

    var currentLocation: Location? = null

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)

        title = intent.getStringExtra(ACTION)

        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Categories.values())

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        when (title) {
            "New Memory" ->
            {
                etDate.setText(SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).format(Date()))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

            }
            "Edit Memory" ->
            {
                idToEdit = intent.getIntExtra(ID, 0)
                setEditData(idToEdit)
            }
            else ->
            {
                Toast.makeText(this, "Unknown Action For Memory", Toast.LENGTH_SHORT).show()
            }
        }

        btnPhoto.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                    takePictureIntent -> takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        btnVideo.setOnClickListener {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
                    takeVideoIntent -> takeVideoIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            }
        }

        btnAudio.setOnClickListener {
            Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION).also {
                    takeAudioIntent -> takeAudioIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takeAudioIntent, REQUEST_AUDIO_CAPTURE)
                }
            }
        }
    }

    private fun getIndexByString(spinner: Spinner, string: String): Int
    {
        var index = 0

        for (i in 0 until spinner.count)
        {
            if (spinner.getItemAtPosition(i).toString().equals(string, ignoreCase = true))
            {
                index = i
                break
            }
        }

        return index
    }

    private fun setEditData(id: Int)
    {
        val memory = MemoriesManager.instance.memories.find { it.id == id }

        etTitle.setText(memory?.title)
        spinner.setSelection(getIndexByString(spinner, memory?.category.toString()))
        etDate.setText((SimpleDateFormat("dd/mm/yyyy").format(memory?.date)))
        etDescription.setText(memory?.description)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_create, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item!!.itemId)
        {
            R.id.save -> {
                val newMemory = Memory(etTitle.text.toString(), spinner.selectedItem.toString(),
                    SimpleDateFormat("dd/mm/yyyy").parse( etDate.text.toString() ),
                    etDescription.text.toString())
                newMemory.id = idToEdit

                when (title)
                {
                    "New Memory" ->
                    {
                        MemoriesManager.instance.addMemory(newMemory)
                        setResult(NEW_MEMORY)
                    }
                    "Edit Memory" ->
                    {
                        MemoriesManager.instance.updateMemory(newMemory)
                        setResult(UPDATE_MEMORY)
                    }
                    else ->
                    {
                        Toast.makeText(this, "Unknown Action For Memory", Toast.LENGTH_SHORT).show()
                    }
                }

                finish()
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgBtnPhoto.setImageBitmap(imageBitmap)
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK)
        {
            val videoUri: Uri = intent.data!!
            vvBtnVideo.setVideoURI(videoUri)
            //val imageBitmap = data?.extras?.get("data") as Bitmap
            //imgBtnVideo.setImageBitmap(imageBitmap)
        }
        else if (requestCode == REQUEST_AUDIO_CAPTURE && resultCode == RESULT_OK)
        {
                val audioUri : Uri = intent.data!!
                // make use of this MediaStore uri
                // e.g. store it somewhere
        }
    }

    override fun onLocationChanged(location: Location?) {
        currentLocation = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

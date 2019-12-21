package es.usj.mastertsa.fm.memoriesapp

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
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.os.Looper
import androidx.core.app.ActivityCompat
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_create_memory.btnAudio
import kotlinx.android.synthetic.main.activity_create_memory.btnPhoto
import kotlinx.android.synthetic.main.activity_create_memory.btnVideo
import kotlinx.android.synthetic.main.activity_create_memory.imgBtnAudio
import kotlinx.android.synthetic.main.activity_create_memory.imgBtnPhoto
import kotlinx.android.synthetic.main.activity_create_memory.mapFragment
import java.io.File
import java.io.IOException


const val ACTION = "New/Edit Memory"
const val ID = "Memory Id"
const val REQUEST_IMAGE_CAPTURE = 1
const val REQUEST_VIDEO_CAPTURE = 2
const val REQUEST_AUDIO_CAPTURE = 3
const val VIEW_MEMORY_PHOTO = 4

class ManageMemory : AppCompatActivity(), LocationListener, MapFragment.MapInterface
{

    var idToEdit: Int = 0
    var location: LatLng = LatLng(0.0,0.0)
    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)

        title = intent.getStringExtra(ACTION)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Categories.values())

        when (title) {
            "New Memory" ->
            {
                etDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION),225)
                    return
                } else
                {
                    locationManager.requestSingleUpdate( LocationManager.NETWORK_PROVIDER,this, Looper.getMainLooper())
                }
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
            takePhoto()
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

    private fun takePhoto()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File ...
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "es.usj.mastertsa.fm.memoriesapp.fileprovider",
                        it
                    )
                    MemoriesManager.instance.tempMemory = Memory(etTitle.text.toString(), spinner.selectedItem.toString(),
                        etDescription.text.toString(), location, currentPhotoPath,
                        SimpleDateFormat("dd/MM/yyyy").parse( etDate.text.toString()))
                    MemoriesManager.instance.tempMemory.id = idToEdit
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun setEditData(id: Int)
    {
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Categories.values())

        val memory = MemoriesManager.instance.findMemory(id)

        etTitle.setText(memory?.title)
        spinner.setSelection(Categories.valueOf(memory?.category.toString()).ordinal)
        etDate.setText((SimpleDateFormat("dd/MM/yyyy").format(memory?.date)))
        etDescription.setText(memory?.description)

        currentPhotoPath = memory?.photoPath
        if (!currentPhotoPath.isNullOrEmpty())
        {
            imgBtnPhoto.setImageResource(R.drawable.photo_available)
            imgBtnPhoto.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_PHOTO_PATH, currentPhotoPath)
                intent.putExtra(MULTIMEDIA, MULTIMEDIA_PHOTO)
                startActivityForResult(intent, VIEW_MEMORY_PHOTO)
            }
        }

        if (!memory?.videoPath.isNullOrEmpty())
        {
            imgBtnVideo.setImageResource(R.drawable.video_available)
        }

        if (!memory?.audioPath.isNullOrEmpty())
        {
            imgBtnAudio.setImageResource(R.drawable.audio_available)
        }

        location = memory?.location!!
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
                    etDescription.text.toString(), location, currentPhotoPath,
                    SimpleDateFormat("dd/MM/yyyy").parse( etDate.text.toString()))
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
            idToEdit = intent.getIntExtra(ID, 0)
            setEditData(idToEdit)
            //galleryAddPic()
            //val imageBitmap = data?.extras?.get("data") as Bitmap
            //imgBtnPhoto.setImageBitmap(imageBitmap)
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK)
        {
            val videoUri: Uri = intent.data!!
            //vvBtnVideo.setVideoURI(videoUri)
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

    override fun locateMap(map: GoogleMap?) {
        if(location != LatLng(0.0, 0.0)) {
            map?.addMarker(MarkerOptions().position(location))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }

    override fun onLocationChanged(p0: Location?)
    {
        location = LatLng(p0!!.latitude, p0!!.longitude)
        (mapFragment as MapFragment).setNewLocation(location)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic()
    {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }
}
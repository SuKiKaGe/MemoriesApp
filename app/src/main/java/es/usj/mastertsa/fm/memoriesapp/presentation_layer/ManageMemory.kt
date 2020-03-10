package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.Manifest
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
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
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.usj.mastertsa.fm.memoriesapp.domain_layer.*
import es.usj.mastertsa.fm.memoriesapp.R
import kotlinx.android.synthetic.main.activity_create_memory.btnAudio
import kotlinx.android.synthetic.main.activity_create_memory.btnPhoto
import kotlinx.android.synthetic.main.activity_create_memory.btnVideo
import kotlinx.android.synthetic.main.activity_create_memory.imgBtnAudio
import kotlinx.android.synthetic.main.activity_create_memory.imgBtnPhoto
import kotlinx.android.synthetic.main.activity_create_memory.imgBtnVideo
import kotlinx.android.synthetic.main.activity_create_memory.mapFragment
import java.io.File
import java.io.IOException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ControlFlowWithEmptyBody")
class ManageMemory : AppCompatActivity(), LocationListener,
    MapFragment.MapInterface
{
    private var idToEdit : Int = 0
    private var location : LatLng = LatLng(0.0,0.0)
    private var currentPhotoPath : String? = null
    private var currentVideoPath : String? = null
    private var currentAudioPath : String? = null
    private var permissionGranted : Boolean = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)

        askForPermission()

        title = intent.getStringExtra(ACTION)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            Categories.values())

        when (title)
        {
            "New Memory" ->
            {
                etDate.setText(SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault()).format(Date()))

                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                imgBtnPhoto.setImageBitmap(ImageManager.getImage(ImageType.photo_not_available))
                imgBtnVideo.setImageBitmap(ImageManager.getImage(ImageType.video_not_available))
                imgBtnAudio.setImageBitmap(ImageManager.getImage(ImageType.audio_not_available))

                if (checkPermissionBoolean())
                {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION),225)
                    return
                }
                else
                {
                    locationManager.requestSingleUpdate( LocationManager.NETWORK_PROVIDER,
                        this, Looper.getMainLooper())
                }
            }

            "Edit Memory" ->
            {
                idToEdit = intent.getIntExtra(ID, 0)
                setEditData(idToEdit)
            }

            else ->
            {
                Toast.makeText(this, "Unknown Action For Memory.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        btnPhoto.setOnClickListener { takePhoto()  }
        btnVideo.setOnClickListener { recordVideo() }
        btnAudio.setOnClickListener {
            try {
                val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                startActivityForResult(intent,
                    REQUEST_AUDIO_CAPTURE
                )
            }catch (e: ActivityNotFoundException)
            {
                Toast.makeText(this, "No default application for recording", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //region Media functionality
    private fun takePhoto()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile : File? = try {
                    createImageFile()
                }
                catch (ex : IOException)
                {
                    // Error occurred while creating the File ...
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                        this,
                        "es.usj.mastertsa.fm.memoriesapp.fileprovider",
                        it
                    )

                    saveTemp()
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,
                        REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile() : File
    {
        // Create an image file name
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun recordVideo()
    {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                val videoFile : File? = try {
                    File.createTempFile(
                        "MP4_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                        /* prefix */
                        ".mp4", /* suffix */
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
                    ).apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        currentVideoPath = absolutePath
                    }
                }
                catch (ex : IOException)
                {
                    // Error occurred while creating the File ...
                    null
                }

                // Continue only if the File was successfully created
                videoFile?.also {
                    val videoURI : Uri = FileProvider.getUriForFile(
                        this,
                        "es.usj.mastertsa.fm.memoriesapp.fileprovider",
                        it
                    )

                    saveTemp()
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                    startActivityForResult(takeVideoIntent,
                        REQUEST_VIDEO_CAPTURE
                    )
                }
            }
        }
    }
    //endregion

    @SuppressLint("SimpleDateFormat")
    private fun saveTemp()
    {
        MemoriesManager.instance.tempMemory =
            Memory(
                idToEdit,
                etTitle.text.toString(),
                spinner.selectedItem.toString(), etDescription.text.toString(), location,
                SimpleDateFormat("dd/MM/yyyy").parse(etDate.text.toString()),
                currentPhotoPath, currentVideoPath, currentAudioPath
            )
    }

    @SuppressLint("SimpleDateFormat")
    private fun setEditData(id : Int = 0)
    {
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            Categories.values())

        val memory = MemoriesManager.instance.findMemory(id)

        etTitle.setText(memory?.title)
        spinner.setSelection(Categories.valueOf(memory?.category.toString()).ordinal)
        etDate.setText((SimpleDateFormat("dd/MM/yyyy").format(memory?.date)))
        etDescription.setText(memory?.description)

        currentPhotoPath = memory?.photoPath

        if (!currentPhotoPath.isNullOrEmpty())
        {
            imgBtnPhoto.setImageBitmap(ImageManager.getImage(ImageType.photo_available))
            imgBtnPhoto.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, currentPhotoPath)
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

        currentVideoPath = memory?.videoPath

        if (!memory?.videoPath.isNullOrEmpty())
        {
            imgBtnVideo.setImageBitmap(ImageManager.getImage(ImageType.video_available))
            imgBtnVideo.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, currentVideoPath)
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

        currentAudioPath = memory?.audioPath

        if (!memory?.audioPath.isNullOrEmpty())
        {
            imgBtnAudio.setImageBitmap(ImageManager.getImage(ImageType.audio_available))
            imgBtnAudio.setOnClickListener {
                val intent = Intent(this, ViewMemoryPhoto::class.java)
                intent.putExtra(CURRENT_MULTIMEDIA_PATH, currentAudioPath)
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

        location = memory?.location!!
    }

    //region Menu
    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.options_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item : MenuItem?) : Boolean
    {
        when(item!!.itemId)
        {
            R.id.save -> {
                val newMemory =
                    Memory(
                        idToEdit,
                        etTitle.text.toString(),
                        spinner.selectedItem.toString(),
                        etDescription.text.toString(),
                        location,
                        SimpleDateFormat("dd/MM/yyyy").parse(etDate.text.toString()),
                        currentPhotoPath,
                        currentVideoPath,
                        currentAudioPath
                    )

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
                        Toast.makeText(this, "Unknown Action For Memory.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                finish()
            }
            else ->
            {
                Toast.makeText(this, "Something was wrong.", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    //endregion

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
    {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_VIDEO_CAPTURE)&& resultCode == RESULT_OK)
        {
            setEditData()
        }
        else if (requestCode == REQUEST_AUDIO_CAPTURE && resultCode == RESULT_OK)
        {
            currentAudioPath = data!!.data!!.toString()
            saveTemp()
            setEditData()
        }
    }

    //region Map functionality
    override fun locateMap(map : GoogleMap?)
    {
        if(location != LatLng(0.0, 0.0))
        {
            map?.addMarker(MarkerOptions().position(location))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }

    override fun onLocationChanged(p0 : Location?)
    {
        location = LatLng(p0!!.latitude, p0.longitude)

        if(mapFragment != null) { (mapFragment as MapFragment).setNewLocation(location, true) }
    }

    override fun onStatusChanged(p0 : String?, p1 : Int, p2 : Bundle?) { }

    override fun onProviderEnabled(p0 : String?) { }

    override fun onProviderDisabled(p0 : String?) { }
    //endregion

    //region Permissions
    private fun askForPermission() = if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
    {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0
            )
        }
        else { }
    }
    else { permissionGranted = true }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<String>,
                                            grantResults : IntArray) =
        when (requestCode)
        {
            0 -> permissionGranted = (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
            else -> permissionGranted = false
        }

    private fun checkPermissionBoolean(): Boolean
    {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
    }
    //endregion
}

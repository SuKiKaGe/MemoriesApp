package es.usj.mastertsa.fm.memoriesapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
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

class ManageMemory : AppCompatActivity()
{
    var idToEdit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)

        title = intent.getStringExtra(ACTION)

        when (title) {
            "New Memory" ->
            {
                etDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
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

    private fun setEditData(id: Int)
    {
        val memory = MemoriesManager.instance.memories.find { it.id == id }

        etTitle.setText(memory?.title)
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
                val newMemory = Memory(etTitle.text.toString(), etDescription.text.toString())
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
}

package es.usj.mastertsa.fm.memoriesapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_view_memory_multimedia.*
import java.io.File

const val CURRENT_MULTIMEDIA_PATH = "Current Photo Path"

const val MULTIMEDIA = "Multimedia"
const val MULTIMEDIA_PHOTO = 41
const val MULTIMEDIA_VIDEO = 42

class ViewMemoryPhoto : AppCompatActivity()
{
    var id = 0
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory_multimedia)

        id = intent.getIntExtra(ID,0)
        val currentPath = intent.getStringExtra(CURRENT_MULTIMEDIA_PATH)
        val multimediaType = intent.getIntExtra(MULTIMEDIA, 40)

        when(multimediaType)
        {
            MULTIMEDIA_PHOTO ->
            {
                imageView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                BitmapFactory.decodeFile(currentPath)?.also { bitmap ->
                    imageView.setImageBitmap(bitmap)
                }
            }
            MULTIMEDIA_VIDEO ->
            {
                imageView.visibility = View.GONE
                videoView.visibility = View.VISIBLE
                var uri = Uri.parse(currentPath)
                videoView.setVideoURI(uri)
                videoView.start()
            }
        }
    }

    override fun onBackPressed()
    {
        val resultIntent = Intent()
        resultIntent.putExtra(ID,id)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

package es.usj.mastertsa.fm.memoriesapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_view_memory_multimedia.*

const val CURRENT_MULTIMEDIA_PATH = "Current Photo Path"
const val MULTIMEDIA = "Multimedia"
const val MULTIMEDIA_PHOTO = 41
const val MULTIMEDIA_VIDEO = 42
const val MULTIMEDIA_AUDIO = 43

class ViewMemoryPhoto : AppCompatActivity()
{
    var id = 0

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory_multimedia)

        id = intent.getIntExtra(ID,0)

        val currentPath = intent.getStringExtra(CURRENT_MULTIMEDIA_PATH)

        when(intent.getIntExtra(MULTIMEDIA, 40))
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
                val uri = Uri.parse(currentPath)
                videoView.setVideoURI(uri)
                videoView.start()
            }

            MULTIMEDIA_AUDIO ->
            {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.audio_playing))
                videoView.visibility = View.GONE
                val uri = Uri.parse(currentPath)
                val mediaPlayer = MediaPlayer.create(this, uri)
                mediaPlayer.start()
            }
        }
    }

    override fun onBackPressed()
    {
        val resultIntent = Intent()
        resultIntent.putExtra(ID, id)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

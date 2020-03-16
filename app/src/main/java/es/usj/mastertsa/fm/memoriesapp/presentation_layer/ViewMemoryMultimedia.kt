package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import es.usj.mastertsa.fm.memoriesapp.domain_layer.*
import es.usj.mastertsa.fm.memoriesapp.R
import kotlinx.android.synthetic.main.activity_view_memory_multimedia.*

class ViewMemoryPhoto : AppCompatActivity()
{
    var id = 0

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory_multimedia)

        id = intent.getIntExtra(ID,0)

        val currentPath : String? = intent.getStringExtra(CURRENT_MULTIMEDIA_PATH)

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
                val uri : Uri = Uri.parse(currentPath)
                videoView.setVideoURI(uri)
                videoView.start()
            }

            MULTIMEDIA_AUDIO ->
            {
                imageView.visibility = View.VISIBLE
                imageView.setImageBitmap(ImageManager.getImage(ImageType.audio_playing))
                videoView.visibility = View.GONE
                val uri : Uri = Uri.parse(currentPath)
                val mediaPlayer : MediaPlayer = MediaPlayer.create(this, uri)
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

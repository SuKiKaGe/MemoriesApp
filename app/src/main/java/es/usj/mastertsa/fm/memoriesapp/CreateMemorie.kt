package es.usj.mastertsa.fm.memoriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_memorie.*
import java.text.SimpleDateFormat
import java.util.*

const val ACTION = "New Memorie"

class CreateMemorie : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memorie)

        setTitle(intent.getStringExtra(ACTION))
        //actionBar.

        etDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_create, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId

        val option = when(itemId)
        {
            R.id.save -> {
                var newMemory = Memory()
                newMemory.title = etTitle.text.toString()
                newMemory.description = etDescription.text.toString()
                MemoriesManager.instance.addMemorie(newMemory)
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}

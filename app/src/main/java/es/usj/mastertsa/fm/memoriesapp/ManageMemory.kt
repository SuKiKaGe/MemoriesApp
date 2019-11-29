package es.usj.mastertsa.fm.memoriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_memory.*
import java.text.SimpleDateFormat
import java.util.*

const val ACTION = "New/Edit Memory"
const val TITLE = "Memory Title"

class ManageMemory : AppCompatActivity()
{
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
                etTitle.setText(intent.getStringExtra(TITLE))
            }
            else ->
            {
                Toast.makeText(this, "Unknown Action For Memory", Toast.LENGTH_SHORT).show()
            }
        }
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
                val newMemory = Memory()
                newMemory.title = etTitle.text.toString()
                newMemory.description = etDescription.text.toString()

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
}

package es.usj.mastertsa.fm.memoriesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


const val NEW_MEMORY = 10
const val UPDATE_MEMORY = 11
const val VIEW_MEMORY = 12

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MemoriesManager.instance.addMemory(Memory("Recuerdo 1", "Descripción de recuerdo 1"))
        MemoriesManager.instance.addMemory(Memory("Recuerdo 2", "Descripción de recuerdo 2"))
        MemoriesManager.instance.addMemory(Memory("Recuerdo 3", "Descripción de recuerdo 3"))

        listMemories.adapter = MemoryAdapter(this)

        listMemories.setOnItemClickListener { parent, _, position, _ ->
            val item = MemoriesManager.instance.memories.get(position)// The item that was clicked

            var intent = Intent(this, ViewMemory::class.java)
            intent.putExtra(ID, item?.id)
            startActivityForResult(intent, VIEW_MEMORY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.options_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item!!.itemId)
        {
            R.id.addMemory -> {
                var intent = Intent(this, ManageMemory::class.java)
                intent.putExtra(ACTION, "New Memory")
                startActivityForResult(intent, NEW_MEMORY)
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        when(requestCode)
        {
            NEW_MEMORY ->
            {
                listMemories.adapter = MemoryAdapter(this)
            }
            VIEW_MEMORY ->
            {
                if(resultCode == UPDATE_MEMORY) listMemories.adapter = MemoryAdapter(this)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}

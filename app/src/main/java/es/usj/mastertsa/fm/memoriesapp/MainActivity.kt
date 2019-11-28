package es.usj.mastertsa.fm.memoriesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

const val NEW_MEMORY = 10

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listMemories.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, MemoriesManager.instance.memories)
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
                var intent = Intent(this, CreateMemorie::class.java)
                intent.putExtra(ACTION, "New Memory")
                startActivityForResult(intent, NEW_MEMORY)
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //TODO: Quitar el test y poner objetos de lista personalizados
        //TODO: Investigar por qué el primer elemento se esconde detrás del actionbar
        var test = ArrayList<String>()

        for (i in MemoriesManager.instance.memories)
        {
            test.add(i.title)
        }

        when(requestCode)
        {
            NEW_MEMORY ->
            {
                listMemories.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, test)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

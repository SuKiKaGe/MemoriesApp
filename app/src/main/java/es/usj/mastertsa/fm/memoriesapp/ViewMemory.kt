package es.usj.mastertsa.fm.memoriesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_memory.*
import java.text.SimpleDateFormat

class ViewMemory : AppCompatActivity()
{
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory)

        if(id == 0) id = intent.getIntExtra(ID, 0)

        setViewContent()
    }

    private fun setViewContent()
    {
        val memory = MemoriesManager.instance.memories.find { it.id == id }
        title = memory?.title
        tvCategoryItem.text = memory?.category.toString()
        tvDateItem.text = (SimpleDateFormat("dd/mm/yyyy").format(memory?.date))
        tvDescription.text = memory?.description
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.options_view, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item!!.itemId)
        {
            R.id.edit -> {
                val intent = Intent(this, ManageMemory::class.java)
                intent.putExtra(ACTION, "Edit Memory")
                intent.putExtra(ID, id)
                startActivityForResult(intent, UPDATE_MEMORY)
            }
            R.id.delete -> {
                MemoriesManager.instance.deleteMemory(id)
                setResult(DELETE_MEMORY)
                finish()
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            UPDATE_MEMORY -> setViewContent()
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        setResult(UNMODIFIED)
    }
}

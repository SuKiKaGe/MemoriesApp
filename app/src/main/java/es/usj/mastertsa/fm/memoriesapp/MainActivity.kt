package es.usj.mastertsa.fm.memoriesapp

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat

const val UNMODIFIED = 0
const val NEW_MEMORY = 10
const val UPDATE_MEMORY = 11
const val VIEW_MEMORY = 12
const val DELETE_MEMORY = 13

enum class Order
{
    Date,
    Name_A_Z,
    Name_Z_A,
    Category_A_Z,
    Category_Z_A
}

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(MemoriesManager.instance.memories.size <= 0) {
            MemoriesManager.instance.addMemory(
                Memory(
                    "Recuerdo 1", "Bad",
                    "Descripción de recuerdo 1", LatLng(130.0, 150.0)
                )
            )
            MemoriesManager.instance.addMemory(
                Memory(
                    "Recuerdo 2", "BlackList",
                    "Descripción de recuerdo 2", LatLng(50.0, 20.0)
                )
            )
            MemoriesManager.instance.addMemory(
                Memory(
                    "Recuerdo 3", "Travel",
                    "Descripción de recuerdo 3", LatLng(200.0, 180.0)
                )
            )
        }

        spOrder.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Order.values())
        spOrder.setSelection(MemoriesManager.instance.orderSelected.ordinal)
        spOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setList(spOrder.selectedItem as Order)
            }

        }

        setList(null)
    }

    fun setList(order: Order?)
    {
        listMemories.adapter = MemoryAdapter(this, MemoriesManager.instance.getMemories(order))

        listMemories.setOnItemClickListener { parent, _, position, _ ->
            val item = MemoriesManager.instance.getMemories(order).get(position)// The item that was clicked

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
            R.id.map ->
            {
                var intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
            else -> Toast.makeText(this, "Something was wrong", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        when(requestCode)
        {
            NEW_MEMORY, UPDATE_MEMORY, VIEW_MEMORY, DELETE_MEMORY ->
            {
                setList(null)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}

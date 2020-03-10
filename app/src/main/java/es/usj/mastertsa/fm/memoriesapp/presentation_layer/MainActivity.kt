package es.usj.mastertsa.fm.memoriesapp.presentation_layer

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import es.usj.mastertsa.fm.memoriesapp.*
import es.usj.mastertsa.fm.memoriesapp.data_layer.model.MemoryModel
import es.usj.mastertsa.fm.memoriesapp.domain_layer.*
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Suppress("EnumEntryName")
enum class Order { Date, Name_A_Z, Name_Z_A, Category_A_Z, Category_Z_A }

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), LocationListener
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MemoriesManager.instance.init(this)

        ImageManager.hoardRequiredImages()

        spOrder.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            Order.values())
        spOrder.setSelection(MemoriesManager.instance.orderSelected.ordinal)
        spOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent : AdapterView<*>?) { }

            override fun onItemSelected(parent : AdapterView<*>?, view : View?, position : Int, id : Long)
            { setList(spOrder.selectedItem as Order) }
        }

        setList(null)

        val locationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),225)
            return
        }
        else
        {
            locationManager.requestSingleUpdate( LocationManager.NETWORK_PROVIDER,
                this, Looper.getMainLooper())
        }

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) requestGPS()
    }

    private fun requestGPS()
    {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Location")
        builder.setMessage("The location must be enable")
        builder.setPositiveButton("YES"){dialog, which ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setNegativeButton("No"){dialog,which ->
            Toast.makeText(applicationContext,"You are not agree.",Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun makeNotificationChannel(id : String, name : String, importance : Int)
    {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(true) // set false to disable badges, Oreo exclusive

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun issueNotification(notification_title : String, notification_text : String)
    {
        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            makeNotificationChannel(
                CHANNEL,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        val notification = NotificationCompat.Builder(this,
            CHANNEL
        )
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
            .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
            .setContentTitle(notification_title)
            .setContentText(notification_text)
            .setNumber(3) // this shows a number in the notification dots

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.notify(1, notification.build())
        // it is better to not use 0 as notification id, so used 1.
    }

    fun setList(order : Order?)
    {
        listMemories.adapter =
            MemoryListViewAdapter(
                this,
                MemoriesManager.instance.getMemories(order)
            )

        listMemories.setOnItemClickListener { _, _, position, _ ->
            val item = MemoriesManager.instance.getMemories(order)[position]
            // The item that was clicked
            val intent = Intent(this, ViewMemory::class.java)
            intent.putExtra(ID, item.id)
            startActivityForResult(intent,
                VIEW_MEMORY
            )
        }
    }

    //region Menu
    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.options_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item : MenuItem?) : Boolean
    {
        when(item!!.itemId)
        {
            R.id.addMemory -> {
                val intent = Intent(this, ManageMemory::class.java)
                intent.putExtra(ACTION, "New Memory")
                startActivityForResult(intent,
                    NEW_MEMORY
                )
            }

            R.id.map ->
            {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }

            else -> Toast.makeText(this, "Something was wrong.",
                Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Map
    override fun onLocationChanged(p0 : Location?)
    {
        val location = LatLng(p0!!.latitude, p0.longitude)

        for (memory in MemoriesManager.instance.getMemories(null))
        {
            val distanceToMemoryLocation : Double =
                (SphericalUtil.computeDistanceBetween(location, memory.location))

            val roundDistanceToMemoryLocation =
                BigDecimal(distanceToMemoryLocation).setScale(2, RoundingMode.HALF_EVEN)

            if (distanceToMemoryLocation <= minimumDistanceToNotifyDangerZone
                && memory.category == Categories.BlackList)
            {
                issueNotification(
                    "BlackList Memory - (${memory.title}) - DANGER ZONE!",
                    "You should not be here, mortal! (Distance is " +
                            "$roundDistanceToMemoryLocation meters)")
            }
        }
    }

    override fun onStatusChanged(p0 : String?, p1 : Int, p2 : Bundle?) { }

    override fun onProviderEnabled(p0 : String?) { }

    override fun onProviderDisabled(p0 : String?) { }
    //endregion

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
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

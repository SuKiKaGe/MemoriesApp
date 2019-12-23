package es.usj.mastertsa.fm.memoriesapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_create_memory.mapFragment

class MapActivity : AppCompatActivity(), LocationListener, MapFragment.MapInterface
{
    private lateinit var location : LatLng

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val locationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(
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
    }

    override fun locateMap(map : GoogleMap?)
    {
        for (memory in MemoriesManager.instance.memories)
        {
            map?.addMarker(MarkerOptions().position(memory.location))
        }

        map?.isMyLocationEnabled = true
    }

    override fun onLocationChanged(p0 : Location?)
    {
        location = LatLng(p0!!.latitude, p0.longitude)
        (mapFragment as MapFragment).setNewLocation(location)
    }

    override fun onStatusChanged(p0 : String?, p1 : Int, p2 : Bundle?) { }

    override fun onProviderEnabled(p0 : String?) { }

    override fun onProviderDisabled(p0 : String?) { }

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.options_map, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item : MenuItem?) : Boolean
    {
        when(item!!.itemId)
        {
            R.id.addMemory -> {
                val intent = Intent(this, ManageMemory::class.java)
                intent.putExtra(ACTION, "New Memory")
                startActivityForResult(intent, NEW_MEMORY)
            }

            R.id.main ->
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            else -> Toast.makeText(this, "Something was wrong.",
                Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
    {
        when(requestCode)
        {
            NEW_MEMORY -> { }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}

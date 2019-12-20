package es.usj.mastertsa.fm.memoriesapp


import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    interface MapInterface
    {
        fun locateMap(map: GoogleMap?)
    }

    private var mMap: GoogleMap? = null
    lateinit var mapInterface : MapInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        getMapAsync(this)
        mapInterface = activity as MapInterface
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onMapReady(p0: GoogleMap?)
    {
        mMap = p0
        mapInterface.locateMap(mMap)
    }

    fun setNewLocation(location: LatLng, addMark: Boolean = true)
    {
        if(addMark) mMap?.addMarker(MarkerOptions().position(location))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

        Toast.makeText(this.context, "Lat: "+ location.latitude + " long: "+ location.longitude, Toast.LENGTH_LONG).show()
    }
}

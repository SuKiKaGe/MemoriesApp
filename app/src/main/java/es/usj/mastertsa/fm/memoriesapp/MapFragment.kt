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

    private var mMap: GoogleMap? = null
    private var latLng: LatLng = LatLng(0.0,0.0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        getMapAsync(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onMapReady(p0: GoogleMap?)
    {
        mMap = p0
        mMap?.addMarker(MarkerOptions().position(latLng))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

    fun setNewLocation(location: Location)
    {
        latLng = LatLng(location.latitude, location.longitude)
        mMap?.addMarker(MarkerOptions().position(latLng))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

        //Toast.makeText(this.context, "Lat: "+ ll.latitude + " long: "+ ll.longitude, Toast.LENGTH_LONG).show()
    }
}

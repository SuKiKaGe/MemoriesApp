package es.usj.mastertsa.fm.memoriesapp.PresentationLayer

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
import com.google.android.gms.maps.model.CameraPosition

class MapFragment : SupportMapFragment(), OnMapReadyCallback
{
    interface MapInterface
    {
        fun locateMap(map : GoogleMap?)
    }

    private var mMap : GoogleMap? = null
    private lateinit var mapInterface : MapInterface

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View?
    {
        getMapAsync(this)
        mapInterface = activity as MapInterface

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onMapReady(p0 : GoogleMap?)
    {
        mMap = p0
        mapInterface.locateMap(mMap)
    }

    fun setNewLocation(location : LatLng, addMarker : Boolean = false)
    {
        if(addMarker) { mMap?.addMarker(MarkerOptions().position(location)) }

        val cameraPosition = CameraPosition.Builder()
            .target(location)     // Sets the center of the map to location user
            .zoom(16f)                   // Sets the zoom
            .build()                   // Creates a CameraPosition from the builder
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}

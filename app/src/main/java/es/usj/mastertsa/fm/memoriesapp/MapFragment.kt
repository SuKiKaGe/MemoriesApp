package es.usj.mastertsa.fm.memoriesapp


import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    var location : Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        getMapAsync(this)

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onMapReady(p0: GoogleMap?)
    {
        var ll = LatLng(location!!.latitude, location!!.longitude)
        p0?.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13f))
        p0?.addMarker(MarkerOptions().position(ll))
    }


}

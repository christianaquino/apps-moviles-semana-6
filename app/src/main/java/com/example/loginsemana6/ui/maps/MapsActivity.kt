package com.example.loginsemana6.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.loginsemana6.R
import com.example.loginsemana6.data.model.Pedido
import com.example.loginsemana6.databinding.ActivityMaps2Binding
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import java.math.RoundingMode
import java.text.DecimalFormat


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Get location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                    val myLocation = LatLng(location.latitude, location.longitude)
                    val pedido = auth.currentUser?.email?.let { Pedido(it, location) }

                    val montoEnvio = if (pedido?.envio == 0) {
                        "¡Gratis!"
                    } else {
                        if (pedido?.envio == -1) {
                            "No contamos con envío a tu dirección"
                        } else "CLP " + pedido?.envio }


                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.CEILING

                    findViewById<TextView>(R.id.total).text = "CLP " + pedido?.monto
                    findViewById<TextView>(R.id.distancia).text = df.format(pedido?.distancia) + " Km"
                    findViewById<TextView>(R.id.envio).text = montoEnvio

                    mMap.addMarker(
                        MarkerOptions().position(myLocation).title(getAddress(location.latitude, location.longitude))
                    )

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.0f))

        }
    }

    private fun getAddress(lat: Double, lng: Double): String? {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat,lng,1)
        return list[0].getAddressLine(0)
    }
}
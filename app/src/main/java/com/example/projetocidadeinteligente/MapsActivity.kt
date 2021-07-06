package com.example.projetocidadeinteligente

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.projetocidadeinteligente.api.EndPoints
import com.example.projetocidadeinteligente.api.Ponto
import com.example.projetocidadeinteligente.api.ServiceBuilder
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pontos: List<Ponto>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var estgLat: Double = 0.0
    private var estgLong: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        estgLat = 41.6935
        estgLong = -8.8467

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllPontos()

        call.enqueue(object : Callback<List<Ponto>> {
            override fun onResponse(call: Call<List<Ponto>>, response: Response<List<Ponto>>) {
                if (response.isSuccessful) {
                    pontos = response.body()!!

                    for (ponto in pontos) {
                        mMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    ponto.lat.toDouble(),
                                    ponto.long.toDouble()
                                )
                            ).title(ponto.titulo)
                        )
                    }
                }
            }
            override fun onFailure(call: Call<List<Ponto>>, t: Throwable)
            {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult)
            {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var location = LatLng(lastLocation.latitude, lastLocation.longitude)

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))

                findViewById<TextView>(R.id.txtcoordenadas).setText(
                    "Lat: " + location.latitude +
                    " - Long: " + location.longitude)

                val address = getAddress(lastLocation.latitude, lastLocation.longitude)
                findViewById<TextView>(R.id.txtmorada).setText("Morada: " + address)

                findViewById<TextView>(R.id.txtdistancia).setText(
                    "Distância: " + calculateDistance(
                        lastLocation.latitude, lastLocation.longitude,
                        estgLat, estgLong
                    ).toString() + " metros"
                )
            }
        }

        createLocationRequest()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}
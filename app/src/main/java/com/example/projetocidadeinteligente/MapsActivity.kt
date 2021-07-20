package com.example.projetocidadeinteligente

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var FlagHAPPY: Int = 0

    private val addPontoActivityRequestCode = 1

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
                        if(ponto.tipo_id == 1)
                        {
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        ponto.lati.toDouble(),
                                        ponto.longi.toDouble()
                                    )
                                ).title(ponto.titulo + " - Acidente")
                            )
                        }
                        else
                            if(ponto.tipo_id == 2)
                            {
                                mMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            ponto.lati.toDouble(),
                                            ponto.longi.toDouble()
                                        )
                                    ).title(ponto.titulo + " - Obras")
                                )
                            }
                        else
                            if(ponto.tipo_id == 3)
                            {
                                mMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            ponto.lati.toDouble(),
                                            ponto.longi.toDouble()
                                        )
                                    ).title(ponto.titulo + " - Etc")
                                )
                            }
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

                if(FlagHAPPY == 0)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
                    FlagHAPPY++
                }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_mapa, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.sharedPref), Context.MODE_PRIVATE)
        var location = LatLng(lastLocation.latitude, lastLocation.longitude)
        return when (item.itemId)
        {
            R.id.optionAdd ->
            {
                val intent = Intent(this@MapsActivity, AddPonto::class.java)
                intent.putExtra("ID", sharedPref.getString("ID_Key", "defaultname"))
                intent.putExtra("LATLONG", location.toString());
                startActivityForResult(intent, addPontoActivityRequestCode)
                true
            }
            R.id.optionLogout ->
            {
                with (sharedPref.edit()) {
                    putString("User_Key", null)
                    putString("Pass_Key", null)
                    putString("ID_Key", null)
                    apply()
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.sharedPref), Context.MODE_PRIVATE)
            if(requestCode == addPontoActivityRequestCode)
            {

                    var pLatLong: LatLng
                    val latlong = data?.getStringExtra(AddPonto.LATLONG)!!.split(",".toRegex()).toTypedArray()
                try
                {
                    val latitude = latlong[0].substring(10, latlong[0].length - 1).toDouble()
                    val longitude = latlong[1].substring(0, latlong[1].length - 1).toDouble()

                    val pTitulo = data?.getStringExtra(AddPonto.ID)
                    pLatLong = LatLng(latitude, longitude)
                    val pTipo = data?.getStringExtra(AddPonto.TIPO)

                if (pTitulo!= null && pTipo != null) {
                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.addPonto(pTitulo, latitude.toString(), longitude.toString(), pTipo,
                        sharedPref.getString("ID_Key", "defaultname")!!.toInt())

                    call.enqueue(object : Callback<Ponto> {
                        override fun onResponse(call: Call<Ponto>, response: Response<Ponto>) {
                            if (response.isSuccessful) {
                                mMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            pLatLong.latitude,
                                            pLatLong.longitude
                                        )
                                    ).title(pTitulo)
                                )
                                Toast.makeText(this@MapsActivity, "Ponto inserido!", Toast.LENGTH_SHORT).show()

                            }
                        }
                        override fun onFailure(call: Call<Ponto>, t: Throwable)
                        {
                            Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                } catch (X: Exception)
                {
                    Toast.makeText(this@MapsActivity, X.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else
        {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
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
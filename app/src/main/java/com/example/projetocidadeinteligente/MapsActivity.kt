package com.example.projetocidadeinteligente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.projetocidadeinteligente.api.EndPoints
import com.example.projetocidadeinteligente.api.Ponto
import com.example.projetocidadeinteligente.api.ServiceBuilder

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(41.6, -8.8)))
    }
}
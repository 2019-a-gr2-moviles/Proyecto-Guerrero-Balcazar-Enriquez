package com.example.proyecto_libreria.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyecto_libreria.R
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var tienePermisosLocalizacion= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        solicitarPermisosLocalizacion()

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
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
//        Log.i("umaps", mMap.myLocation.latitude.toString())
//        Log.i("umaps", mMap.myLocation.longitude.toString())



        // Add a marker in Sydney and move the camera
        val foch = LatLng(-0.202760, -78.490813)

        //  mMap.addMarker(MarkerOptions().position(fo).title("Marker in Sydney"))
//        mMap.addMarker(MarkerOptions()
//            .position(foch)
//            .title("Marker in Sydney"))
        val titulo= "foch"
        val local1 = LatLng(-0.19241020000000003, -78.5092769)
        val local2 = LatLng(-0.19159481301055337, -78.49482515781544)
        anadirMarcado(local1 ,"SUCURSAL 1")
        anadirMarcado(local2 ,"SUCURSAL 2")
        val poliLineaUno= googleMap.addPolyline(
            PolylineOptions()
                .clickable(true).add(
                    local1,
                    LatLng(-0.208218, -78.490163)

                )
        )

        moverCamara(local1, 17f)
        establecerconfiguracionMapa(mMap)


    }
    fun anadirMarcado(latLng: LatLng, titulo:String ){
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(titulo)
        )
//        Log.i("umaps", mMap.myLocation.latitude.toString())
//        Log.i("umaps", mMap.myLocation.longitude.toString())
    }
    fun moverCamara(latLng: LatLng, zoom: Float = 10f){

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )

    }
    fun establecerconfiguracionMapa(mapa: GoogleMap){
        val contexto= this.applicationContext
        with(mapa){
            val permisFineLocation = ContextCompat.checkSelfPermission(
                contexto,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            val tienePermission = permisFineLocation== PackageManager.PERMISSION_GRANTED
            if(tienePermission) {
                mapa.isMyLocationEnabled = true
            }

            this.uiSettings.isZoomControlsEnabled=true
            uiSettings.isMyLocationButtonEnabled=true
        }
    }
    fun solicitarPermisosLocalizacion(){
        val permisFineLocation = ContextCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val tienePermission = permisFineLocation== PackageManager.PERMISSION_GRANTED
        if(tienePermission){
            Log.i("mapa", "TIENE PERMISO")
            this.tienePermisosLocalizacion=true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1 //CÓDIGO QUE VAMOS A ESPERAR
            )
        }
    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_maps)
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    private fun checkPermissions(): Boolean {
//        val permissionState = ActivityCompat.checkSelfPermission(this,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION)
//        return permissionState == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val local1 = LatLng(-0.19241020000000003, -78.5092769)
//        val local2 = LatLng(-0.19159481301055337, -78.49482515781544)
//
//
//        mMap.addMarker(MarkerOptions().position(local1).title("Local 1"))
//        val addMarker = mMap.addMarker(MarkerOptions().position(local2).title("Local 2"))
//
//        mMap.setMinZoomPreference(10.0f);
//        mMap.setMaxZoomPreference(24.0f);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(local1))
//    }
//    fun solicitarPermisosLocalizacion(){
//        val permisFineLocation = ContextCompat.checkSelfPermission(
//            this.applicationContext,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        val tienePermission = permisFineLocation== PackageManager.PERMISSION_GRANTED
//        if(tienePermission){
//            Log.i("mapa", "TIENE PERMISO")
//            this.tienePermisosLocalizacion=true
//        }else{
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                1 //CÓDIGO QUE VAMOS A ESPERAR
//            )
//        }
//    }
}

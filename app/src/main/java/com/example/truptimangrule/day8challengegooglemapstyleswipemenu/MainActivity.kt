package com.example.truptimangrule.day8challengegooglemapstyleswipemenu

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : FragmentActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {


   val TAG:String?="MainActivity"


    var mMap: GoogleMap? = null
    var locationManager: LocationManager?=null
    var locationListener: LocationListener?=null
    private val PERMISSION_REQUEST_CAMERA = 0
    var criteria: Criteria?= Criteria()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        locationListener = this
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val currentapiVersion = Build.VERSION.SDK_INT

        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {

            criteria?.speedAccuracy = Criteria.ACCURACY_HIGH
            criteria?.accuracy = Criteria.ACCURACY_FINE
            criteria?.isAltitudeRequired = true
            criteria?.isBearingRequired = true
            criteria?.isSpeedRequired = true

        }

    }
    private fun getMyLocation() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,""+locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 15f, locationListener)
            val provider = locationManager?.getBestProvider(criteria, true)

            val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null && mMap!=null) {
                Log.d("location",""+location)
                onLocationChanged(location)
            }else{
                Log.d("location","null")
            }
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission()
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the [android.Manifest.permission.CAMERA] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.


        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation()
            } else {
                // Permission request was denied.

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onLocationChanged(location: Location?) {
        Log.d(TAG,"  "+location!!.latitude+"  ,  "+ location.longitude+""+mMap)
        val sydney = LatLng(location!!.latitude, location.longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18));
        val target = CameraPosition.builder().target(sydney).zoom(17f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null)    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    override fun onMapReady(p0: GoogleMap?) {
        Log.d(TAG, "onMapReady: ")
        mMap = p0
        getMyLocation()
    }
}

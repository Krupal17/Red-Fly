package com.example.mymap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.AvoidType
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.example.mymap.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    var TAG = "-----"
    private lateinit var binding: ActivityMapsBinding

    //    var ListofLocation: ArrayList<LatLng> = ArrayList()
    private lateinit var locationManager: LocationManager
    private var location: Location? = null
    lateinit var curr_latLng: LatLng
    var polyline: Polyline? = null
    private val LOCATION_MIN_UPDATE_TIME = 10
    private val LOCATION_MIN_UPDATE_DISTANCE = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }

    private fun initview() {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(object : SupportMapFragment(), OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                mMap = p0
//                Log.e("TAG", "onMapReady: $mMap", )
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.13216336637465, 55.18539918066694), 10.0f))
//                for (i in 0 until ListofLocation.size) {
//                    // Add a marker in Sydney and move the camera
////                val location = LatLng(-34.0, 151.0)
//                    mMap.addMarker(MarkerOptions().position(ListofLocation[i]).title(i.toString()))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ListofLocation[i]))
//                }
                getCurrentLocation()

                mMap.setOnMapClickListener {
                    drawMarker(it, false)
                    drawline(it.latitude, it.longitude, TransportMode.WALKING)
                }
            }

        })

    }

    fun getCurrentLocation() {
        try {
            Log.e(TAG, "getCurrentLocation: ===")
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(TAG, "getCurrentLocation:== ")
                val isGPSEnabled =
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


                if (!isGPSEnabled && !isNetworkEnabled) {
                    //                    showToast(getString(R.string.please_on_your_GPS_location));
                    Toast.makeText(this, "please_on_your_GPS_location", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.e(TAG, "getCurrentLocation: ")
                    location = null
                    if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            LOCATION_MIN_UPDATE_TIME.toLong(),
                            LOCATION_MIN_UPDATE_DISTANCE.toFloat(),
                            locationListener
                        )
                        location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    }
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            LOCATION_MIN_UPDATE_TIME.toLong(),
                            LOCATION_MIN_UPDATE_DISTANCE.toFloat(),
                            locationListener
                        )
                        location =
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                    if (location != null) {

                        curr_latLng = LatLng(location!!.latitude, location!!.longitude)
                        drawMarker(curr_latLng, true)


//                        drawline(21.226920897072215, 72.83169425889456, TransportMode.DRIVING)
                        return
                    } else {
                        getCurrentLocation()
                    }
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        12
                    )
                }
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        13
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "getCurrentLocation: ${e.message}")
            e.printStackTrace()
            onBackPressed()
        }
    }

    private fun drawMarker(latLng: LatLng, flag: Boolean) {
        if (mMap != null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
//                        markerOptions.title(title);
            if (flag) {
                markerOptions.icon(BitmapFromVector(this, R.drawable.dest_loc));
//                markerOptions.icon(BitmapFromVector(this, R.drawable.download))
                mMap.addMarker(markerOptions)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            } else {
                Log.e("TAG", "drawMarker: mark")
                markerOptions.icon(BitmapFromVector(this, R.drawable.current_loc))
//            }
                mMap.addMarker(markerOptions)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            }
            Log.e(TAG, "drawMarker: ${latLng.latitude.toLong()}")
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
//
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }


    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun drawline(lat: Double, longt: Double, mode: String) {
        try {
            mMap.clear()
            drawMarker(curr_latLng, false)
//            if (isfromDetailScreen) {
            drawMarker(LatLng(lat, longt), true)
            //            }
            GoogleDirection.withServerKey("API_KEY")
                .from(LatLng(curr_latLng.latitude, curr_latLng!!.longitude))
                .to(LatLng(lat, longt))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .transportMode(mode)
                .execute(object : DirectionCallback {
                    override fun onDirectionSuccess(direction: Direction?) {
                        directionsuccess(direction)
                    }

                    override fun onDirectionFailure(t: Throwable) {}
                })
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TAG", "drawline:exce " + e.message)
        }
    }

    private fun directionsuccess(direction: Direction?) {
        try {
            if (direction!!.isOK) {
                val route = direction.routeList[0]
                if (route != null && !route.legList.isEmpty()) {
                    val distance = route.legList[0].distance
                    val duration = route.legList[0].duration
                    val directionPositionList = route.legList[0].directionPoint
                    if (!directionPositionList.isEmpty()) {
                        if (polyline != null) {
                            polyline!!.remove()
                        }
                        polyline = mMap!!.addPolyline(
                            DirectionConverter.createPolyline(
                                this,
                                directionPositionList,
                                4,
                                ContextCompat.getColor(this, R.color.purple_200)
                            )
                        )
//                        setCameraWithCoordinationBounds(route)
                    } else {
                        Toast.makeText(this, "noroute_available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "noroute_available", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "noroute_available", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
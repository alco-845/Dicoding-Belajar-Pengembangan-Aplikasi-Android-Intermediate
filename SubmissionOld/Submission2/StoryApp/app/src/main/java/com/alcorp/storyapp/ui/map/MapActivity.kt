package com.alcorp.storyapp.ui.map

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.storyapp.R
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.databinding.ActivityMapBinding
import com.alcorp.storyapp.helper.PrefData
import com.alcorp.storyapp.model.ListStory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        if (checkNetwork(this)){
            addMapMarker()
        } else {
            Toast.makeText(this@MapActivity, resources.getString(R.string.toast_network), Toast.LENGTH_SHORT).show()
        }
        setMapStyle()
    }

    private fun addMapMarker() {
        Toast.makeText(this, getString(R.string.toast_wait), Toast.LENGTH_SHORT).show()

        val service = ApiConfig.getApiService().getStoryLocation(PrefData.token, 1)
        service.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                val responseBody = response.body()
                if (responseBody != null && !responseBody.error) {
                    responseBody.listStory.forEach { story ->
                        val latLng = LatLng(story.lat, story.lon)
                        mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
                    }
                    Toast.makeText(this@MapActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MapActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                Toast.makeText(this@MapActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setMapStyle() {
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
    }

    private fun checkNetwork(context: Context?): Boolean{
        val cm: ConnectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                cm.getNetworkCapabilities(cm.activeNetwork)
            } else {
                cm.activeNetworkInfo
            }
        return network != null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
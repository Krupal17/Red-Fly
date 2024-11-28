package com.narine.kp.redfly.addemo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val initCountdown: Long
    get() = initCountdown_

var initCountdown_: Long = 8L

val triggerSpeedFetch: Long
    get() {
        val measuereDownload = MeasuereDownload()
        CoroutineScope(Dispatchers.IO).launch {
            val speedMbps =
//                measuereDownload.measureDownloadSpeedMbps("https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_92x30dp.png")
                measuereDownload.measureDownloadSpeedMbps("https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
            measuereDownload.calculateCountdownDuration(speedMbps).also { initCountdown_ = it }
            d("RedFlyAd", "measuereDownload:$initCountdown_   speedMbps:$speedMbps ")
        }
        return initCountdown_
    }

val Context.isOnline: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
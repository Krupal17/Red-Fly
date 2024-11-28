package com.narine.kp.redfly.addemo.utils

import java.net.URL
import kotlin.system.measureTimeMillis

class MeasuereDownload(){
    suspend fun measureDownloadSpeedMbps(testUrl: String): Double {
        val downloadSizeBytes = URL(testUrl).openConnection().contentLength // File size in bytes
        var speedMbps = 0.0
        val timeTakenMillis = measureTimeMillis {
            URL(testUrl).openStream()
        }

        d("AdLoader", "Download timeTakenMillis: --$timeTakenMillis")

        if (timeTakenMillis > 0) {
            // Convert to Mbps: (Bytes * 8 bits) / (time in ms * 1000 to get seconds) / 1M for Mbps
            speedMbps = (downloadSizeBytes * 8.0) / (timeTakenMillis * 1000.0)
            d("AdLoader", "Download speed: ${"%.2f".format(speedMbps)} Mbps")
        }

        return speedMbps
    }

    fun calculateCountdownDuration(speedMbps: Double): Long {
        return when {
            speedMbps > 4 -> 4000L // Fast - 4 seconds
            speedMbps > 2 -> 7000L // Moderate - 7 seconds
            speedMbps > 0.5 -> 10000L // Slower - 10 seconds
            else -> 12000L // Slowest - 12 seconds
        }
    }

}

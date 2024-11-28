package com.narine.kp.redfly.addemo.utils

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import com.narine.kp.redfly.addemo.adHelp.redFlyAd.TAG
import com.narine.kp.redfly.addemo.shared.SubscriptionPreferences
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// App Status

var subscriptionPreferences: SubscriptionPreferences? = null
val isSubs: Boolean
    get() = subs_

var subs_ = false

var adBlocker = false

fun Context.setAdBlocker() {
    val adServerUrl = "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"
    try {
        val connection = URL(adServerUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 3000 // 3 seconds timeout
        connection.connect()
        adBlocker = connection.responseCode != HttpURLConnection.HTTP_OK
    } catch (e: IOException) {
        // If there's an exception, we assume the ad is blocked
        adBlocker = true
    }
}

fun initAppStatus(app: Application) {
    subscriptionPreferences = SubscriptionPreferences(app)
    // sub set
    subs_ = subscriptionPreferences?.isSubscribed() ?: false
    // count down set
    app.apply {
        if (isOnline) {
            initialize(this)
            triggerSpeedFetch
            setAdBlocker()
        }
    }

}

// App - Sub and network
val Context.adApproval: Boolean
    get() = isOnline && !isSubs && !adBlocker

fun d(tag: String, message: String) {
    Log.d(tag, message)
    writeLogToFile("DEBUG", tag, message)
}

private var isInitialized = false
private lateinit var logFile: File

// Initialize log file (call once, for example, in Application class)
fun initialize(context: Context) {
    val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (!downloadsDir.exists()) {
        downloadsDir.mkdirs()
    }
    logFile = File(downloadsDir, "Red Fly Logs")
    Log.d(TAG, "LogToFile is Now Initialized.")
    isInitialized = true
}

// Private method to write the log to the file
private fun writeLogToFile(level: String, tag: String, message: String) {
    if (!isInitialized) {
        Log.d(tag, "LogToFile is not initialized. Call initialize() first.")
        return
    }
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    val logMessage = "$timestamp - $tag: $message\n\n"

    try {
        val writer = FileWriter(logFile, true)
        writer.append(logMessage)
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun deleteLogs() {
    logFile.delete()
}
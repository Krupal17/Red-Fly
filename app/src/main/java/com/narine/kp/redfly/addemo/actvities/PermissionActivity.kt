package com.narine.kp.redfly.addemo.actvities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.narine.kp.redfly.addemo.utils.initialize

class PermissionActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestStoragePermission()
    }

    private fun checkAndRequestStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // For Android 11 and above, request MANAGE_EXTERNAL_STORAGE permission
                if (!Environment.isExternalStorageManager()) {
                    requestManageExternalStoragePermission()
                } else {
                    onPermissionGranted()
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // For Android 6.0 to 10, request WRITE_EXTERNAL_STORAGE permission
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    onPermissionGranted()
                }
            }

            else -> {
                // For Android 5.1 and below, no runtime permission required
                onPermissionGranted()
            }
        }
    }

    private fun requestManageExternalStoragePermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
            Uri.parse("package:$packageName")
        )
        storagePermissionLauncher.launch(intent)
    }

    // Handle result for MANAGE_EXTERNAL_STORAGE permission
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    // Handle WRITE_EXTERNAL_STORAGE permission result for Android 6.0 to 10
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }

    private fun onPermissionGranted() {
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        // Proceed with your logic that requires storage access
        initialize(this@PermissionActivity)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onPermissionDenied() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        // Inform the user or take appropriate action
    }
}

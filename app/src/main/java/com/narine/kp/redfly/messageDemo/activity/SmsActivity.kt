package com.narine.kp.redfly.messageDemo.activity

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.narine.kp.redfly.R
import com.narine.kp.redfly.messageDemo.DatabaseRepo
import com.narine.kp.redfly.messageDemo.DefaultSmsAppHelper
import com.narine.kp.redfly.messageDemo.Manager.fetchAllSms
import com.narine.kp.redfly.messageDemo.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsActivity : AppCompatActivity() {
    private lateinit var smsHelper: DefaultSmsAppHelper
    private lateinit var smsRepository: DatabaseRepo
    private val PERMISSION_REQUEST_CODE = 101

    private val defaultSmsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Check if the app is now the default SMS app
                smsRepository = DatabaseRepo().apply {
                    initDatabase(this@SmsActivity)
                }
                if (smsHelper.isDefaultSmsApp()) {
                    lifecycleScope.launch {

                        fetchAllSms(this@SmsActivity)
                        val smsList = smsRepository.getAllSms()

                        withContext(Dispatchers.Main) {
                            Log.e("TAG-->", ": ${smsList.size}")

                        }
                    }
//                    Toast.makeText(this, "App is now the default SMS app", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        val smsList = smsRepository.getAllSms()
                        Log.e("TAG-->", "2 : ${smsList.size}")

                    }
//                    Toast.makeText(this, "Failed to set as default SMS app", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
        smsHelper = DefaultSmsAppHelper(this)

        // Get instance
        val preferenceManager = SharedPreferenceManager.getInstance(this)
        val isFirstTime = preferenceManager.isFirstTime()

        if (isFirstTime) {
            preferenceManager.setIsFirstTime(false)
            Log.e("TAG-->", "Mango : be isFirstTime")

            checkSmsPermissions()

        } else {
//            lifecycleScope.launch {
//                val smsList = smsRepository.getAllSms()
//                Log.e("TAG-->", "2 : ${smsList.size}")
//
//            }
        }
    }

    private fun checkSmsPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.e("TAG-->", "onRequestPermissionsResult: ", )
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted
                CoroutineScope(Dispatchers.IO).launch {
                Log.e("TAG-->", "onRequestPermissionsResult: 2", )
                    fetchAllSms(this@SmsActivity)
                }
            } else {
                // Permissions denied
            }
        }
    }

    private fun checkAndRequestSmsRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.e("TAG-->", "checkAndRequestSmsRole: ")
            val roleManager =
                getSystemService(ROLE_SERVICE) as RoleManager
            if (!roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                // Request to become the default SMS app
                Log.e("TAG-->", "checkAndRequestSmsRole:isRoleHeld ")
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
                defaultSmsLauncher.launch(intent)
            } else {
                // App already set as default SMS app
                Toast.makeText(this, "App is already the default SMS app", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            val defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(this)
            if (defaultSmsPackage != packageName) {
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                startActivity(intent)
            } else {
                // App is already the default SMS app
                Toast.makeText(this, "App is already the default SMS app", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
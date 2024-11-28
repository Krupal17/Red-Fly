package com.narine.kp.redfly.addemo.actvities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.narine.kp.redfly.addemo.adHelp.redFlyAd.loadInterstitialAd
import com.narine.kp.redfly.addemo.adHelp.redFlyAd.showInterstitialAd
import com.narine.kp.redfly.addemo.utils.initialize
import com.narine.kp.redfly.databinding.ActivitySplashBinding
import com.narine.kp.redfly.messageDemo.DatabaseRepo
import com.narine.kp.redfly.messageDemo.Manager.repoRef
import com.narine.kp.redfly.messageDemo.activity.SmsActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initviews()

        lifecycleScope.launch {
            repoRef = DatabaseRepo()
            repoRef.initDatabase(this@SplashActivity)
        }

    }

    private fun initviews() {
        loadInterstitialAd(this)
        startCountdown{
//            toAdDemo()
        toSmsDemo()
        }
    }

    private fun toSmsDemo() {
        startActivity(Intent(this@SplashActivity, SmsActivity::class.java))
    }

    private fun toAdDemo() {
        checkAndRequestStoragePermission { permissionGranted ->
            showInterstitialAd {
                if (permissionGranted) {
                    initialize(this@SplashActivity)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            PermissionActivity::class.java
                        )
                    )
                    finish()
                }

            }
        }
    }

    private fun startCountdown(action:()->Unit) {
        val countdownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update countdown text every second
                binding.txtCountDown.text = "Remain for Open Ad ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                // Final message when countdown ends
                binding.txtCountDown.text = "Done!"
             action()
            }
        }
        countdownTimer.start()
    }

    private fun checkAndRequestStoragePermission(permissionGranted: (Boolean) -> Unit) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // For Android 11 and above, request MANAGE_EXTERNAL_STORAGE permission
                if (!Environment.isExternalStorageManager()) {
                    permissionGranted(false)
                } else {
                    permissionGranted(true)
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // For Android 6.0 to 10, request WRITE_EXTERNAL_STORAGE permission
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionGranted(false)
                } else {
                    permissionGranted(true)
                }
            }

            else -> {
                // For Android 5.1 and below, no runtime permission required
                permissionGranted(true)
            }
        }
    }

}
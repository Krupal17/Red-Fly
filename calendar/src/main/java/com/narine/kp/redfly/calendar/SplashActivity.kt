package com.narine.kp.redfly

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.narine.kp.redfly.mvc.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Load data in a coroutine
        lifecycleScope.launch {
            val repository = CalendarRepository()
            repository.loadAllYearData() // Load calendar data

            // Once data is loaded, start MainActivity
            startMainActivity()
        }
    }
    private suspend fun startMainActivity() {
        // Use withContext to switch to the main thread for UI operations
        withContext(Dispatchers.Main) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Close SplashActivity
        }
    }
}
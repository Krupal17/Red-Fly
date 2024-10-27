package com.narine.kp.redfly

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.narine.kp.redfly.mvvm.YearRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Start loading data in the background during splash screen
        CoroutineScope(Dispatchers.IO).launch {
            if (!YearRepository.isDataLoaded()) {
                YearRepository.loadAllYearData()
            }

            // Simulate splash screen delay (optional)
            delay(2000)

            // After data is loaded, start MainActivity
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                startActivity(Intent(this@SplashActivity, MainActivity2::class.java))
                startActivity(Intent(this@SplashActivity, MainActivity3::class.java))
                finish()
            }
        }
    }
}
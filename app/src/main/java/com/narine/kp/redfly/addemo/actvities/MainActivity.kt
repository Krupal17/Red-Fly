package com.narine.kp.redfly.addemo.actvities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.narine.kp.redfly.addemo.adHelp.redFlyAd.runTimeRewardedAd
import com.narine.kp.redfly.addemo.adHelp.redFlyAd.runTimeShowInterstitialAd
import com.narine.kp.redfly.addemo.utils.deleteLogs
import com.narine.kp.redfly.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

    }

    private fun initView() {

        binding.apply {
            btnShowInterstitial.setOnClickListener {
                runTimeShowInterstitialAd {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }

            btnRewardShow.setOnClickListener {
                runTimeRewardedAd { isRewarded, message ->
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteLogs()
    }
}



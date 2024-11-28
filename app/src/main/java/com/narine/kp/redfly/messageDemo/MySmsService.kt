package com.narine.kp.redfly.messageDemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MySmsService : Service() {
    var repo: DatabaseRepo? = null
    override fun onCreate() {
        super.onCreate()
        repo = DatabaseRepo().apply {
            initDatabase(this@MySmsService)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Get SMS data from the intent
        val sender = intent?.getStringExtra("sender")
        val message = intent?.getStringExtra("message")
        val time =
            intent?.getLongExtra("time", System.currentTimeMillis()) ?: System.currentTimeMillis()

        if (sender != null && message != null) {
            // Save SMS to database in a coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val smsEntity = SmsEntity(
                    address = sender,
                    body = message,
                    date = time,
                )
                repo?.insertSms(smsEntity)
            }
        }

        // Stop the service after processing
        stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Not used for this service
        return null
    }

    private fun storeSmsInDatabase(sender: String, message: String) {
        // Example function to save SMS in the Room database
        // val smsEntity = SmsEntity(sender = sender, message = message)
        // smsDao.insertSms(smsEntity)
    }
}

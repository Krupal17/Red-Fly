package com.narine.kp.redfly.messageDemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class MySmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                try {
                    // Retrieve SMS data
                    val pdus = bundle.get("pdus") as Array<*>?
                    if (pdus != null) {
                        for (pdu in pdus) {
                            val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                            val sender = sms.displayOriginatingAddress
                            val messageBody = sms.messageBody
                            val time = sms.timestampMillis

                            // Log or process the SMS
                            Log.d("MySmsReceiver", "SMS Received from: $sender, Message: $messageBody")

                            // Optionally pass the data to a service
                            val serviceIntent = Intent(context, MySmsService::class.java)
                            serviceIntent.putExtra("sender", sender)
                            serviceIntent.putExtra("message", messageBody)
                            serviceIntent.putExtra("time", time)
                            context.startService(serviceIntent)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MySmsReceiver", "Error processing SMS", e)
                }
            }
        }
    }
}

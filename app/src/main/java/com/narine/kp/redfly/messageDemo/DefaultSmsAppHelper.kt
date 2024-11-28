package com.narine.kp.redfly.messageDemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class DefaultSmsAppHelper(private val context: Context) {

    // Check if this app is the default SMS app
    fun isDefaultSmsApp(): Boolean {
        val defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(context)
        return defaultSmsPackage == context.packageName
    }

    // Prompt the user to set this app as the default SMS app
    fun promptToSetDefaultSmsApp(activity: Activity) {
        if (!isDefaultSmsApp()) {
            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.packageName)
            activity.startActivity(intent)
        }
    }
}

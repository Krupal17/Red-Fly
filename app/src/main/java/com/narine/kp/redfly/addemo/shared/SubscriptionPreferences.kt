package com.narine.kp.redfly.addemo.shared

import android.content.Context
import android.content.SharedPreferences

class SubscriptionPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "SubscriptionPrefs"
        private const val KEY_IS_SUBSCRIBED = "isSubscribed"
        private const val KEY_SUBSCRIPTION_TYPE = "subscriptionType"
        private const val KEY_SUBSCRIPTION_START_DATE = "subscriptionStartDate"
        private const val KEY_SUBSCRIPTION_END_DATE = "subscriptionEndDate"
    }

    // Set subscription status and details
    fun setSubscriptionStatus(
        isSubscribed: Boolean,
        type: String = "",
        startDate: Long = 0L,
        endDate: Long = 0L
    ) {
        prefs.edit().apply {
            putBoolean(KEY_IS_SUBSCRIBED, isSubscribed)
            putString(KEY_SUBSCRIPTION_TYPE, type)
            putLong(KEY_SUBSCRIPTION_START_DATE, startDate)
            putLong(KEY_SUBSCRIPTION_END_DATE, endDate)
        }.apply()
    }

    // Check if the user is subscribed
    fun isSubscribed(): Boolean {
        return prefs.getBoolean(KEY_IS_SUBSCRIBED, false)
    }

    // Get subscription type (e.g., monthly, yearly)
    fun getSubscriptionType(): String? {
        return prefs.getString(KEY_SUBSCRIPTION_TYPE, "")
    }

    // Get subscription start date
    fun getSubscriptionStartDate(): Long {
        return prefs.getLong(KEY_SUBSCRIPTION_START_DATE, 0L)
    }

    // Get subscription end date
    fun getSubscriptionEndDate(): Long {
        return prefs.getLong(KEY_SUBSCRIPTION_END_DATE, 0L)
    }

    // Check if the subscription is still active based on the end date
    fun isSubscriptionActive(): Boolean {
        val endDate = getSubscriptionEndDate()
        return endDate > System.currentTimeMillis()
    }

    // Clear all subscription details
    fun clearSubscriptionData() {
        prefs.edit().clear().apply()
    }
}

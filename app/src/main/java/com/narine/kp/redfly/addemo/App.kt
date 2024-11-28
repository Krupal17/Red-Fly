package com.narine.kp.redfly.addemo

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.narine.kp.redfly.addemo.utils.initAppStatus
import com.narine.kp.redfly.addemo.utils.setAdBlocker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class App : Application(), /*LifecycleObserver,*/ ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        val thread = CoroutineScope(Dispatchers.IO)
        thread.launch {
            MobileAds.initialize(this@App)
            initAppStatus(this@App)
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
        setAdBlocker()
    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

}
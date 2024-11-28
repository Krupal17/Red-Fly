package com.narine.kp.redfly.addemo.adHelp

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.narine.kp.redfly.addemo.utils.adApproval
import com.narine.kp.redfly.addemo.utils.d
import com.narine.kp.redfly.addemo.utils.initCountdown

object redFlyAd {

    val TAG = "RedFlyAd"
    var flexibleAdMode = false
    var isInterstitialRequested = false
    var isOpenAdRequested = false
    var isRewardedAdRequested = false
    var isRequestedAds = isInterstitialRequested || isRewardedAdRequested


    var interstitialAd: InterstitialAd? = null
    var openAd: AppOpenAd? = null
    var rewardedAd: RewardedAd? = null

    var interstitialAdUnit = "ca-app-pub-3940256099942544/1033173712"
    var openAdUnit = "ca-app-pub-3940256099942544/9257395921"
    var rewardAdUnit = "ca-app-pub-3940256099942544/5224354917"

    //OpenAd
    fun loadOpenAd(context: Context) {

    }

    fun showAdIfAvailable() {

    }

    //Interstitial
    fun loadInterstitialAd(context: Activity) {
        if (context.adApproval) {
            if (!isInterstitialRequested && interstitialAd == null) {
                val adRequest = AdRequest.Builder().build()
                isInterstitialRequested = true
                InterstitialAd.load(
                    context,
                    interstitialAdUnit,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            adError.toString().let { d(TAG, it) }
                            interstitialAd = null
                            isInterstitialRequested = false
                        }

                        override fun onAdLoaded(interstitialAd_: InterstitialAd) {
                            d(TAG, "Ad was loaded.")
                            isInterstitialRequested = false
                            interstitialAd = interstitialAd_
                        }
                    })
            }
        }
    }

    fun Activity.showInterstitialAd(action: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    d(TAG, "Ad dismissed fullscreen content.")
                    interstitialAd = null
                    action()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    d(TAG, "Ad failed to show fullscreen content.")
                    interstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    d(TAG, "Ad showed fullscreen content.")
                }
            }
            interstitialAd?.show(this)
        } else {
            action()
        }
    }

    fun Activity.runTimeShowInterstitialAd(action: (String) -> Unit) {
        if (adApproval) {
            if (interstitialAd != null) {
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        d(TAG, "Ad dismissed fullscreen content.")
                        interstitialAd = null
                        action("Ad dismissed")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when ad fails to show.
                        d(TAG, "Ad failed to show fullscreen content.")
                        interstitialAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        d(TAG, "Ad showed fullscreen content.")
                    }
                }
                interstitialAd?.show(this)
            } else {
                var isFinished = false
                if (!isInterstitialRequested) {
                    val countdown = initCountdown
                    d(TAG, "runTimeShowInterstitialAd: countdown $countdown ")
                    val countdownTimer = object : CountDownTimer(countdown, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Update countdown text every second
                        }

                        override fun onFinish() {
                            isFinished = true
                            // Final message when countdown ends
                            action("countdown finished")
                        }
                    }

                    val adRequest = AdRequest.Builder().build()
                    isInterstitialRequested = true
                    InterstitialAd.load(
                        this,
                        interstitialAdUnit,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                adError.toString().let { d(TAG, it) }
                                interstitialAd = null
                                isInterstitialRequested = false
                                countdownTimer.cancel()
                            }

                            override fun onAdLoaded(interstitialAd_: InterstitialAd) {
                                d(TAG, "Ad was loaded.")
                                isInterstitialRequested = false
                                countdownTimer.cancel()
                                interstitialAd = interstitialAd_
                                interstitialAd?.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdClicked() {
                                            // Called when a click is recorded for an ad.
                                            d(TAG, "Ad was clicked.")
                                        }

                                        override fun onAdDismissedFullScreenContent() {
                                            // Called when ad is dismissed.
                                            d(TAG, "Ad dismissed fullscreen content.")
                                            interstitialAd = null
                                            action("Ad dismissed")
                                        }

                                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                            // Called when ad fails to show.
                                            d(TAG, "Ad failed to show fullscreen content.")
                                            interstitialAd = null
                                        }

                                        override fun onAdImpression() {
                                            // Called when an impression is recorded for an ad.
                                            d(TAG, "Ad recorded an impression.")
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            // Called when ad is shown.
                                            d(TAG, "Ad showed fullscreen content.")
                                        }
                                    }
                                if (!isFinished) {
                                    interstitialAd?.show(this@runTimeShowInterstitialAd)
                                }
                            }
                        })


                    countdownTimer.start()
                } else {
                    action("ad requested!")
                }
            }
        } else {
            if (interstitialAd != null) {
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        d(TAG, "Ad dismissed fullscreen content.")
                        interstitialAd = null
                        action("Ad dismissed")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when ad fails to show.
                        d(TAG, "Ad failed to show fullscreen content.")
                        interstitialAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        d(TAG, "Ad showed fullscreen content.")
                    }
                }
                interstitialAd?.show(this)
            } else {
                action("ad can't show")
            }
        }
    }

    //Rewards
    fun Activity.runTimeRewardedAd(action: (Boolean, String) -> Unit) {
        if (adApproval) {
            if (rewardedAd != null) {
                showRewardedAd(action)
            } else {
                var isFinished = false
                if (!isRewardedAdRequested) {
                    val countdown = initCountdown
                    d(TAG, "runTimeShowInterstitialAd: countdown $countdown ")

                    val countdownTimer = object : CountDownTimer(countdown, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Update countdown text every second
                        }

                        override fun onFinish() {
                            isFinished = true
                            // Final message when countdown ends
                            if (rewardedAd != null) {
                                showRewardedAd(action)
                            } else {
                                action(false, "countdown finished")
                            }
                        }
                    }
                    countdownTimer.start()

                    var adRequest = AdRequest.Builder().build()
                    isRewardedAdRequested = true
                    RewardedAd.load(this,
                        rewardAdUnit,
                        adRequest,
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                d(TAG, adError.toString())
                                rewardedAd = null
                                isRewardedAdRequested = false
                                countdownTimer.cancel()
                            }

                            override fun onAdLoaded(ad: RewardedAd) {
                                d(TAG, "Ad was loaded.")
                                rewardedAd = ad
                                if (!isFinished) {
                                    countdownTimer.cancel()
                                    rewardedAd?.fullScreenContentCallback =
                                        object : FullScreenContentCallback() {
                                            override fun onAdClicked() {
                                                // Called when a click is recorded for an ad.
                                                d(TAG, "Ad was clicked.")
                                            }

                                            override fun onAdDismissedFullScreenContent() {
                                                // Called when ad is dismissed.
                                                // Set the ad reference to null so you don't show the ad a second time.
                                                d(TAG, "Ad dismissed fullscreen content.")
                                                rewardedAd = null
                                            }

                                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                                // Called when ad fails to show.
                                                d(TAG, "Ad failed to show fullscreen content.")
                                                rewardedAd = null
                                            }

                                            override fun onAdImpression() {
                                                // Called when an impression is recorded for an ad.
                                                d(TAG, "Ad recorded an impression.")
                                            }

                                            override fun onAdShowedFullScreenContent() {
                                                // Called when ad is shown.
                                                d(TAG, "Ad showed fullscreen content.")
                                            }
                                        }
                                    rewardedAd?.show(this@runTimeRewardedAd,
                                        object : OnUserEarnedRewardListener {
                                            override fun onUserEarnedReward(p0: RewardItem) {
                                                action(true, "User earned reward")
                                            }
                                        })
                                }
                                isRewardedAdRequested = false
                            }
                        })
                } else {
                    val countdown = initCountdown
                    d(TAG, "runTimeShowInterstitialAd: countdown $countdown ")

                    val countdownTimer = object : CountDownTimer(countdown - 2, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Update countdown text every second
                        }

                        override fun onFinish() {
                            isFinished = true
                            // Final message when countdown ends
                            if (rewardedAd != null) {
                                showRewardedAd(action)
                            } else {
                                action(false, "countdown finished")
                            }
                        }
                    }
                    countdownTimer.start()
                }
            }
        } else {

        }
    }

    fun Activity.showRewardedAd(action: (Boolean, String) -> Unit) {

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                d(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                d(TAG, "Ad showed fullscreen content.")
            }
        }
        rewardedAd?.show(this, object : OnUserEarnedRewardListener {
            override fun onUserEarnedReward(p0: RewardItem) {
                action(true, "User earned reward")
            }
        })
    }

    fun loadRewardAd(context: Activity) {
        if (context.adApproval) {
            if (!isRewardedAdRequested && rewardedAd == null) {
                var adRequest = AdRequest.Builder().build()
                isRewardedAdRequested = true
                RewardedAd.load(context,
                    rewardAdUnit,
                    adRequest, object : RewardedAdLoadCallback() {
                        override fun onAdLoaded(ad: RewardedAd) {
                            super.onAdLoaded(ad)
                            d(TAG, "Ad was loaded.")
                            rewardedAd = ad
                            isRewardedAdRequested = false
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            super.onAdFailedToLoad(adError)
                            d(TAG, adError.toString())
                            rewardedAd = null
                            isRewardedAdRequested = false
                        }
                    })
            }
        }
    }
}

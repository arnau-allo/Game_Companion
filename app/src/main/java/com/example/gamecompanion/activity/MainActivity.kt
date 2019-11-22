package com.example.gamecompanion.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamecompanion.R
import com.example.gamecompanion.fragment.ProfileFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem -> when(menuItem.itemId){
            R.id.chat -> {
                FirebaseAnalytics.getInstance(this).logEvent("Chat_Tab_Click", null)
            }
            R.id.news -> {
                FirebaseAnalytics.getInstance(this).logEvent("News_Tab_Click", null)
                //TODO: Entregable
            }
            R.id.profile -> {
                //Create fragment
                val profileFragment = ProfileFragment()
                //Add fragment to Fragment Container
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(fragmentContainer.id, profileFragment)
                fragmentTransaction.commit()

                FirebaseAnalytics.getInstance(this).logEvent("Profile_Tab_Click", null)
            }
            R.id.home ->{
                FirebaseAnalytics.getInstance(this).logEvent("Home_Tab_Click", null)
            }

            }
            true
        }
    }
}

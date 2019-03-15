package com.express.pizza.pdq.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.express.pizza.pdq.R
import com.express.pizza.pdq.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val gifListener = GlideUtils.GifListener {
            startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            this.finish()
        }

        Glide.get(this).clearMemory()
        GlideUtils.loadOneTimeGif(this, R.drawable.logo_splash, splashLogo, gifListener)
    }

    override fun onBackPressed() {
        return
    }
}

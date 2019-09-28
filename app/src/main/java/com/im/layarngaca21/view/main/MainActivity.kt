package com.im.layarngaca21.view.main


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.im.layarngaca21.R
import android.graphics.drawable.Animatable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        vp_main.adapter = ViewPagerAdapter(this, supportFragmentManager)
        vp_main.addOnPageChangeListener(viewPagerListener)
        tabs.setupWithViewPager(vp_main)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private val viewPagerListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            if(position==0){
                val ivLogoAnimation = AnimatedVectorDrawableCompat.create(this@MainActivity, R.drawable.ic_tv_anim)
                iv_logo.setImageDrawable(ivLogoAnimation)
                ivLogoAnimation?.start()
                ivLogoAnimation?.start()
            }else{
                val ivLogoAnimation = AnimatedVectorDrawableCompat.create(this@MainActivity, R.drawable.ic_roll_anim)
                iv_logo.setImageDrawable(ivLogoAnimation)
                ivLogoAnimation?.start()
                ivLogoAnimation?.start()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

}

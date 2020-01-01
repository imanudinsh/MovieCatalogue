package com.im.topmovie.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.im.topmovie.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp_main.adapter = ViewPagerAdapter(this, supportFragmentManager)
        vp_main.addOnPageChangeListener(viewPagerListener)
        tabs.setupWithViewPager(vp_main)





    }




    private val viewPagerListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            if(position==0){
                val ivLogoAnimation = AnimatedVectorDrawableCompat.create(this@MainActivity, R.drawable.ic_tv_anim)
                iv_logo.setImageDrawable(ivLogoAnimation)
                ivLogoAnimation?.start()
            }else{
                val ivLogoAnimation = AnimatedVectorDrawableCompat.create(this@MainActivity, R.drawable.ic_roll_anim)
                iv_logo.setImageDrawable(ivLogoAnimation)
                ivLogoAnimation?.start()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

}

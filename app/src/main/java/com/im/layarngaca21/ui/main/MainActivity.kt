package com.im.layarngaca21.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.im.layarngaca21.R
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.core.app.ActivityOptionsCompat
import android.util.Log
import androidx.core.util.Pair as UtilPair
import android.view.View
import com.im.layarngaca21.utils.values.SharedPreferenesEnum
import com.im.layarngaca21.ui.favorite.FavoriteActivity
import com.im.layarngaca21.ui.reminder.ReminderReceiver
import com.im.layarngaca21.ui.reminder.ReminderSettingActivity
import com.im.layarngaca21.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.toolbar


class MainActivity : AppCompatActivity() {

    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        vp_main.adapter = ViewPagerAdapter(this, supportFragmentManager)
        vp_main.addOnPageChangeListener(viewPagerListener)
        tabs.setupWithViewPager(vp_main)


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val releaseRemainderActive = sharedPreferences.getBoolean(SharedPreferenesEnum.RELEASE_REMINDER.value, true)
        val dailyRemainderActive = sharedPreferences.getBoolean(SharedPreferenesEnum.DAILY_REMINDER.value, true)

        reminderReceiver = ReminderReceiver()

        val isReleaseReminderActive = reminderReceiver.isReminderSet(this, SharedPreferenesEnum.RELEASE_REMINDER.value )
        val isDailyReminderActive = reminderReceiver.isReminderSet(this, SharedPreferenesEnum.DAILY_REMINDER.value )

        Log.d("MainActivity", "release reminder $isReleaseReminderActive set $releaseRemainderActive")
        Log.d("MainActivity", "daily reminder $isDailyReminderActive set $dailyRemainderActive")


        if(!isDailyReminderActive&& dailyRemainderActive){
            reminderReceiver.setRepeatingReminder(this, SharedPreferenesEnum.DAILY_REMINDER.value,"07:00", resources.getString(R.string.daily_reminder_msg))
        }

        if(!isReleaseReminderActive&& releaseRemainderActive){
            reminderReceiver.setRepeatingReminder(this, SharedPreferenesEnum.RELEASE_REMINDER.value,"08:00",resources.getString(R.string.daily_reminder_msg))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pairTransition = UtilPair.create<View, String>(toolbar, "container")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairTransition)
        if (item.getItemId() == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }else if(item.getItemId() == R.id.action_reminder_setting){
            val intent = Intent(this, ReminderSettingActivity::class.java)
            startActivity(intent, options.toBundle())
        }else if(item.getItemId() == R.id.action_favorite){
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent, options.toBundle())
        }else if(item.getItemId() == R.id.action_search){
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent, options.toBundle())
        }
        return super.onOptionsItemSelected(item)
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

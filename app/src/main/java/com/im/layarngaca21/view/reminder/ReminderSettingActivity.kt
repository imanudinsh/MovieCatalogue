package com.im.layarngaca21.view.reminder

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import com.im.layarngaca21.R
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.values.SharedPreferenesEnum
import com.im.layarngaca21.utils.values.ToastEnum
import kotlinx.android.synthetic.main.activity_reminder_setting.*

class ReminderSettingActivity : AppCompatActivity() {


    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_title.text = resources.getString(R.string.reminder_setting_title)


        reminderReceiver = ReminderReceiver()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val releaseRemainderActive = sharedPreferences.getBoolean(SharedPreferenesEnum.RELEASE_REMINDER.value, true)
        val dailyRemainderActive = sharedPreferences.getBoolean(SharedPreferenesEnum.DAILY_REMINDER.value, true)

        switch_release.isChecked = releaseRemainderActive
        switch_daily.isChecked = dailyRemainderActive

        switch_release.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean(SharedPreferenesEnum.RELEASE_REMINDER.value, isChecked).apply()
            if (isChecked){
                reminderReceiver.setRepeatingReminder(this, SharedPreferenesEnum.RELEASE_REMINDER.value,"08:00")
            }else{
                reminderReceiver.cancelReminder(this, SharedPreferenesEnum.RELEASE_REMINDER.value)
            }

            val msg = if (isChecked) resources.getString(R.string.release_reminder_active_msg) else resources.getString(R.string.release_reminder_off_msg)
            CustomToast().show(this, msg, ToastEnum.SUCCESS.value)
        }
        switch_daily.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean(SharedPreferenesEnum.DAILY_REMINDER.value, isChecked).apply()
            if (isChecked){
                reminderReceiver.setRepeatingReminder(this, SharedPreferenesEnum.DAILY_REMINDER.value,"07:00",resources.getString(R.string.daily_reminder_msg))
            }else{
                reminderReceiver.cancelReminder(this, SharedPreferenesEnum.DAILY_REMINDER.value)
            }

            val msg = if (isChecked) resources.getString(R.string.daily_reminder_active_msg) else resources.getString(R.string.daily_reminder_off_msg)
            CustomToast().show(this, msg, ToastEnum.SUCCESS.value)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}

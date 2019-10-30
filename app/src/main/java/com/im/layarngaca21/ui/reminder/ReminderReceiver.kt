package com.im.layarngaca21.ui.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.util.Log
import com.google.gson.Gson
import com.im.layarngaca21.BuildConfig
import com.im.layarngaca21.R
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.model.MovieResponse
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.values.ResponseCodeEnum
import com.im.layarngaca21.utils.values.ToastEnum
import com.im.layarngaca21.ui.moviedetail.MovieDetailActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_RELEASE = "releaseReminder"
        const val TYPE_DAILY = "dailyReminder"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val ID_RELEASE = 100
        const val ID_DAILY = 101

        private const val TIME_FORMAT = "HH:mm"

    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)


        val notifId = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_DAILY
        Log.d("ReminderReceiver"," on reveice $type $TYPE_DAILY ${type.equals(TYPE_DAILY, ignoreCase = true)} $message $notifId")
        
        if (type.equals(TYPE_RELEASE, ignoreCase = true)){
            loadReleaseMovie(context)
        }
        if (type.equals(TYPE_DAILY, ignoreCase = true)){
            showReminderNotification(context, message)
        }
    }


    private fun loadReleaseMovie(ctx: Context) {
        val client = AsyncHttpClient()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.MOVIE_API_KEY}&primary_release_date.gte=$currentDate&primary_release_date.lte=$currentDate"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray) {
                if(statusCode== ResponseCodeEnum.OK.code){
                    val movieResponse = Gson().fromJson(String(responseBody), MovieResponse::class.java)
                    showReleaseReminderNotification(ctx, movieResponse.results)
                }


            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error.message)
            }
        })
    }




    private fun showReminderNotification(context: Context, message: String) {
        Log.d("ReminderReceiver"," Daily reminder")

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "DailyReminder channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val reminderSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Layar Ngaca 21")
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(reminderSound)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(ID_DAILY, notification)

    }

    private fun showReleaseReminderNotification(context: Context,listMovie: List<Movie>?) {
        Log.d("ReminderReceiver","$listMovie")
        val CHANNEL_ID = "Channel_2"
        val CHANNEL_NAME = "ReleaseReminder channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val reminderSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        listMovie?.forEach {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.EXTRA_FILM, it)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, it.id.toInt(), intent, 0)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(it.title)
                .setContentText("${it.title} has been release today ! 🎉🎉🎉")
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(reminderSound)

            builder.setChannelId(CHANNEL_ID)

            val notification = builder.build()
            notificationManagerCompat.notify(it.id.toInt(), notification)
        }


    }


    fun setRepeatingReminder(context: Context, type: String, time: String, message: String? = null) {

        if (isDateInvalid(time, TIME_FORMAT)) return

        val reminderManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val now = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val reminderTime = if (calendar.timeInMillis <= now.timeInMillis) calendar.timeInMillis+ (AlarmManager.INTERVAL_DAY+1) else calendar.timeInMillis



        val requestCode = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        reminderManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY, pendingIntent)

    }


    fun cancelReminder(context: Context, type: String) {
        val reminderManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        reminderManager.cancel(pendingIntent)

        val msg = if (type.equals(TYPE_RELEASE, ignoreCase = true)) context.resources.getString(R.string.release_reminder_off_msg) else context.resources.getString(R.string.daily_reminder_off_msg)
        CustomToast().show(context, msg, ToastEnum.SUCCESS.value)
    }


    fun isReminderSet(context: Context, type: String): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_DAILY

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}

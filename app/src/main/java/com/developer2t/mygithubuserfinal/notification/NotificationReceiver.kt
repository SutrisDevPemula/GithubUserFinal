package com.developer2t.mygithubuserfinal.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.developer2t.mygithubuserfinal.MainActivity
import com.developer2t.mygithubuserfinal.R

import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ID = "id"
        const val ID_REMAINDER = 200
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra(EXTRA_ID, 0)

        if (notifId == ID_REMAINDER) {
            val title = context.resources.getString(R.string.daily_remainder)
            val message = context.resources.getString(R.string.message_daily_remainder)
            showNotification(context, title, message, notifId)
        }
//        else {
////            setMovieRelease(context)
//        }
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val chanelId = "Channel_1"
        val channelName = "User Git"

        val intent = Intent(context, MainActivity::class.java)


        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManagerCompat = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, chanelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                chanelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(chanelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRemainder(context: Context, hour: Int, minute: Int, notifId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(EXTRA_ID, notifId)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        var message = context.resources.getString(R.string.daily_remainder)

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun cancelRemainder(context: Context, notifId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        var message = context.resources.getString(R.string.daily_remainder)


        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

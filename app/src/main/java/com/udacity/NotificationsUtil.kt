package com.udacity

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.udacity.MainActivity.Companion.CHANNEL_ID

private val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(
    status: Int,
    filename: Int,
    applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .apply {
            putExtra(DetailActivity.EXTRA_DOWNLOAD_STATUS, status)
            putExtra(DetailActivity.EXTRA_DOWNLOAD_FILE, filename)
        }

    val contentPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val action =
        NotificationCompat.Action(
            R.drawable.ic_download_cloud,
            "Show details",
            contentPendingIntent
        )

    val downloadImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_launcher_background
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    ).apply {
        setSmallIcon(R.drawable.ic_download_cloud)
        setContentTitle("LoadApp downloads")
        setContentText("Information about the requested download is available.")
        setAutoCancel(true)
        setContentIntent(contentPendingIntent)
        addAction(action)
        setLargeIcon(downloadImage)
    }

    notify(NOTIFICATION_ID, builder.build())
}








package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity.Companion.CHANNEL_ID

private val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(
    status: Int,
    filename: Int,
    applicationContext: Context
) {
    val intent = Intent(applicationContext, DetailActivity::class.java)
        .apply {
            putExtra(DetailActivity.EXTRA_IS_SUCCESS, status)
            putExtra(DetailActivity.EXTRA_FILE_NAME, filename)
        }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val action =
        NotificationCompat.Action(
            R.drawable.ic_download_cloud,
            "Show details",
            pendingIntent
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
        setContentIntent(pendingIntent)
        addAction(action)
        setLargeIcon(downloadImage)
    }

    notify(NOTIFICATION_ID, builder.build())
}








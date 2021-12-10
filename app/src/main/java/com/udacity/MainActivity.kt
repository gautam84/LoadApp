package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url: String = ""
    private var fileNum: Int = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()

        createChannel(CHANNEL_ID, "hh")



        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download(url)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query()
                query.setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val returnedError =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.i("Download", "Success")

                        notificationManager.sendNotification(1, fileNum, applicationContext)


                    }

                    if (status == DownloadManager.STATUS_FAILED) {
                        Log.i("Download", "Failed")
                        Log.i("Download ", returnedError.toString())

                        notificationManager.sendNotification(0, fileNum, applicationContext)


                    }

                }
            }


        }
    }

    private fun download(url: String) {
        if (url.isNotEmpty()) {
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request) // enqueue puts the download request in the queue.}
        } else {
            Toast.makeText(
                this,
                "Please select the file to download",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        private const val LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_PROJECT = "https://github.com/bumptech/glide"
        private const val RETROFIT_PROJECT = "https://github.com/square/retrofit"

        const val CHANNEL_ID = "channelId"
    }

    fun selectDownload(view: android.view.View) {
        when (view.id) {
            R.id.glide -> {
                url = GLIDE_PROJECT
                fileNum = 0
            }
            R.id.loadApp -> {
                url = LOAD_APP
                fileNum = 1
            }
            R.id.retrofit -> {
                url = RETROFIT_PROJECT
                fileNum = 2
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.download_status_notificaton)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}

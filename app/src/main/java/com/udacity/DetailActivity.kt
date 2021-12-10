package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DOWNLOAD_FILE = "extra_download_file"
        const val EXTRA_DOWNLOAD_STATUS = "extra_download_status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        val status = (intent.getIntExtra(EXTRA_DOWNLOAD_STATUS, 0))

        val filename = when (intent.getIntExtra(EXTRA_DOWNLOAD_FILE, 0)) {
            0 -> getString(R.string.glide_desc)
            1 -> getString(R.string.load_app_desc)
            2 -> getString(R.string.retrofit_desc)
            else -> "Unknown error occurred"
        }


        file_name.text = filename
        file_status.text =
            if (status == 1) getString(R.string.success) else getString(R.string.failure)
        file_status.setTextColor(if (status == 1) Color.GREEN else Color.RED)

        details_ok_button.setOnClickListener {
            finish()
        }
    }

}

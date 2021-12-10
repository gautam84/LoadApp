package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val isSuccessful = intent.getIntExtra(EXTRA_IS_SUCCESS, 0)

        val filename = when (intent.getIntExtra(EXTRA_FILE_NAME, 0)) {
            0 -> getString(R.string.glide_desc)
            1 -> getString(R.string.load_app_desc)
            2 -> getString(R.string.retrofit_desc)
            else -> "Unknown error occurred"
        }

        file_name.text = filename
        file_status.text =
            if (isSuccessful == 1) getString(R.string.success) else getString(R.string.failure)
        file_status.setTextColor(if (isSuccessful == 1) Color.GREEN else Color.RED)

        details_ok_button.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_FILE_NAME = "extra_file_name"
        const val EXTRA_IS_SUCCESS = "extra_download_status"
    }


}

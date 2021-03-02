package com.example.watchlist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.*

class UpdateAdminWatchListActivity : AppCompatActivity() {

    companion object {
        internal val IMAGE_PICK_CODE = 1000
        internal val PERMISSION_CODE = 1001
        private var imageToUpload =
            Uri.parse("android.resource://your.package.here/drawable/image_name")

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_admin_watch_list)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val getImgBtn = this.findViewById<Button>(R.id.updateAdminImageButton)
        val updateDateBtn = this.findViewById<Button>(R.id.updateAdminDateBtn)
        val updateTimeBtn = this.findViewById<Button>(R.id.updateAdminTimeBtn)
        val updateBtn = this.findViewById<Button>(R.id.updateAdminButton)
        val id = intent.getLongExtra("updateTitles", 0)

        getImgBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, UpdateWatchListActivity.PERMISSION_CODE)
                } else {
                    pickImgFromGallary()
                }
            } else {
                pickImgFromGallary()
            }

        }

        updateDateBtn.setOnClickListener {
            val calender = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            var date = CreateWatchListActivity.Format.format(selectedDate.time).toString()
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = CreateWatchListActivity.Format.format(selectedDate.time).toString()

                    updateDateBtn.text = date
                    Toast.makeText(this, "date:$date", Toast.LENGTH_SHORT).show()
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        updateTimeBtn.setOnClickListener {
            val klock = Calendar.getInstance()
            val selectedTime = Calendar.getInstance()
            var Time = CreateWatchListActivity.timeFormat.format(selectedTime.time).toString()

            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    updateTimeBtn.text = Time
                    Toast.makeText(
                        this,
                        "Time : ${CreateWatchListActivity.timeFormat.format(klock.time)}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                klock.get(Calendar.HOUR_OF_DAY), klock.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }

        updateBtn.setOnClickListener() {
            val updateTitle = this.findViewById<EditText>(R.id.updateAdminTitleEditText).editableText.toString().trim()
            val updateContent = this.findViewById<EditText>(R.id.updateAdminContentEditText).editableText.toString().trim()
            val updatePlatform = this.findViewById<EditText>(R.id.updateAdminPlatformEditText).editableText.toString().trim()

            try {
                newestWatchListRepository.updateAdminsWatchListById(
                    id,
                    updateTitle,
                    updateContent,
                    "Unknown",
                    updatePlatform,
                    imageToUpload,
                    this
                )
                finish()
            } catch (e: IllegalStateException){
                Toast.makeText(this, getString(e.message!!.toInt()), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImgFromGallary(){
        intent = Intent()
        intent.type= "image/*"
        intent.action= Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CreateWatchListActivity.IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            imageToUpload = data?.data
            val img = findViewById<ImageView>(R.id.updateAdminImage)
            img.setImageURI(data?.data)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

            CreateWatchListActivity.PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImgFromGallary()
                } else {
                    Toast.makeText(this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

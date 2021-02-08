package com.example.watchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_menu)
        findViewById<Button>(R.id.profileButtom).setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }
    }




}
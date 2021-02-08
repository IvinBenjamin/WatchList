package com.example.watchlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_menu)

        val backgroundVideoPlayer = this.findViewById<VideoView>(R.id.mainMenuBackground);
        val newestTitles = this.findViewById<Button>(R.id.newestTitles)
        val myWatchlist = this.findViewById<Button>(R.id.myWatchlist)

        val uri = Uri.parse("android.resource://"
                + packageName +"/"
                +R.raw.background);

        backgroundVideoPlayer.setVideoURI(uri);
        backgroundVideoPlayer.start();

        backgroundVideoPlayer.setOnCompletionListener{
            backgroundVideoPlayer.seekTo(0);
            backgroundVideoPlayer.start()
        }

        newestTitles.setOnClickListener(){
            val intent = Intent(this, newestTitles::class.java)
            startActivity(intent)
        }

        myWatchlist.setOnClickListener(){
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)

        findViewById<Button>(R.id.profileButtom).setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))

        }

    }


}
package com.example.watchlist

import android.net.Uri


public class Watch(
        val Id: Long = 0,
        var Title:String = "",
        var Content:String = "",
        var Date:String = "",
        var Platform: String = "",
        var Img: String = ""
){
}

public class NewestWatch(
        val Id : Long = 0,
        var Title : String = "",
        var Content : String = "",
        var Date : String = "",
        var Platform : String = "",
        var Link : String = "",
        var Img : String = ""
){
}

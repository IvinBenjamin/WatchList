package com.example.watchlist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*


open class WatchlistFirebase {


    private var storageRef = Firebase.storage.reference

    fun addWatchListFirebase(id:Long,title: String, content: String, date: String, img: Uri, platform: String){

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        if (currentUser != null) {
            try {

                watch["Title"] = title
                watch["Content"] = content
                watch["Date"] = date
                watch["Id"] = id
                watch["Platform"] = platform
                watch["Img"] = "images/${currentUser.uid}/$id"
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .set(watch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }

            } catch (e: IllegalStateException) {
                throw e
            }
        } else {
            throw error(R.string.authError)
        }
    }

    fun addWatchListFirebase(id:Long,title: String, content: String, date: String, img: String, platform: String){

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        if (currentUser != null) {
            try {
                watch["Title"] = title
                watch["Content"] = content
                watch["Date"] = date
                watch["Id"] = id
                watch["Platform"] = platform
                watch["Img"] = img
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .set(watch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }

            } catch (e: IllegalStateException) {
                throw e
            }
        } else {
            throw error(R.string.authError)
        }
    }


     fun updateWatchListFirebase(
        id: Long,
        newTitle: String,
        newContent: String,
        newDate: String,
        newPlatform: String,
        newImg: Uri,
    ) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val watch = HashMap<String, Any>()
        try {
            if (currentUser != null) {
                watch["Title"] = newTitle
                watch["Content"] = newContent
                watch["Date"] = newDate
                watch["Id"] = id
                watch["Platform"] = newPlatform
                watch["Img"] = "images/${currentUser.uid}/$id"
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .set(watch)
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }

                uploadImgToStorage(id, newImg)
            } else {
                throw error(R.string.authError)
            }
        } catch (e: IllegalStateException) {
            throw e
        }
    }


    fun getDataFromFirebase(context: Context){
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val databaseRef = database.collection("Users").document(currentUser!!.uid).collection("Titles")

        databaseRef.get()
            .addOnCompleteListener{
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT)
                for(document in it.result!!){
                    val title = document.data.getValue("Title") as String
                    val content = document.data.getValue("Content") as String
                    val date = document.data.getValue("Date") as String
                    val img = document.data.getValue("Img") as String
                    val platform = document.data.getValue("Platform") as String
                    val id = document.data.getValue("Id") as Long

                    watchListRepository.addtoWatchlistRepository(title, content, date, img, platform, id)

                }
            }.addOnSuccessListener {
                Toast.makeText(context, "it worked", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "it did not work worked", Toast.LENGTH_SHORT).show()
            }

    }

    fun uploadImgToStorage(id: Long ,imgUri : Uri): StorageTask<UploadTask.TaskSnapshot> {
        val currentUser = Firebase.auth.currentUser
        try {
            if(currentUser != null) {
                val imgPath = storageRef.child("images/${currentUser.uid}/$id")

                return imgPath.putFile(imgUri)
            }else{
                throw error(R.string.authError)
            }
        }catch (e : IllegalAccessException){
            throw e
        }

    }


    fun deleteWatchListFirebase(id: Long) {
        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        try {
            if (currentUser != null) {
                database.collection("Users").document(currentUser.uid).collection("Titles")
                    .document(id.toString())
                    .delete()
                    .addOnFailureListener {
                        throw error(R.string.error)
                    }
                val imgPath = storageRef.child("images/${currentUser.uid}/$id")
                imgPath.delete().addOnFailureListener{
                    throw error(it)
                }
            }else{
                throw error(R.string.authError)
            }
        }catch (e: IllegalStateException){
            throw e
        }

    }

}
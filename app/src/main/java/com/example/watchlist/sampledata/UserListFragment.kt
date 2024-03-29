package com.example.watchlist.sampledata




import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.*
import com.example.watchlist.databinding.FragmentUserListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.common.util.CollectionUtils.isEmpty
import com.google.common.collect.Iterables.isEmpty
import com.google.common.collect.Iterables.size
import com.google.common.collect.Iterators.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import okio.Utf8.size
import java.nio.file.Files.size


class UserListFragment : Fragment() {

    class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        lateinit var binding: FragmentUserListBinding
        lateinit var button: Button
        private var storageRef = Firebase.storage.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    )= FragmentUserListBinding.inflate(inflater, container, false).run {
        binding = this
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("Users").document(currentUser!!.uid).collection(
            "Titles")
        val options : FirestoreRecyclerOptions<Watch> = FirestoreRecyclerOptions.Builder<Watch>().setQuery(
            collectionReference,
            Watch::class.java)
            .setLifecycleOwner(this@UserListFragment).build()

        val adapter = object: FirestoreRecyclerAdapter<Watch, WatchViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchViewHolder {

                val view : View = LayoutInflater.from(parent.context).inflate(
                    R.layout.titles_layout,
                    parent,
                    false
                )
                return WatchViewHolder(view)
            }

            override fun onBindViewHolder(holder: WatchViewHolder, position: Int, model: Watch) {

                val movieTitle : TextView = holder.itemView.findViewById(R.id.movieTitle)
                val moviePlatform : TextView = holder.itemView.findViewById(R.id.moviePlatform)
                val moviePoster : ImageView = holder.itemView.findViewById(R.id.moviePoster)
                //val movieDate : TextView = holder.itemView.findViewById(R.id.movieDate)
                val progressBar: ProgressBar = holder.itemView.findViewById(R.id.progressBar)
                val imgReference = model.Img
                val pathReference = storageRef.child(imgReference)
                pathReference.downloadUrl.addOnSuccessListener{
                    progressBar.visibility = View.GONE
                    Picasso.get().load(it).into(moviePoster)
                }.addOnFailureListener{
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, getString(R.string.downloadError), Toast.LENGTH_SHORT).show()
                }

                movieTitle.text = model.Title
                moviePlatform.text = model.Platform
                //movieDate.text = model.Date


                holder.itemView.setOnClickListener{
                    val intent = Intent(context, WatchViewActivity::class.java).apply {
                        putExtra("id", model.Id)
                    }
                    startActivity(intent)
                }
            }
        }
        userListRecyclerView.adapter = adapter
        userListRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()
        root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addToUserList.setOnClickListener {
            val i = Intent(activity, CreateWatchListActivity::class.java)
            activity?.startActivity(i)
        }
    }
}

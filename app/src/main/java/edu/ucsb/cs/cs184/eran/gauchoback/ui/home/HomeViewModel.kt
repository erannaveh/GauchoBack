package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.eran.gauchoback.ui.post.PostViewModel.Post
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel : ViewModel() {
    private var database = Firebase.database
    private var posts = MutableLiveData<ArrayList<Post>>()

    fun setPosts(args: Bundle?){
        var postType = ""
        var keywords = listOf<String>()
        var ref = database.getReference("/Posts").orderByChild("date")
        if(args != null){
            Log.d("TAG", "Args not null")
            postType = args.getString("postType").toString()
            if(postType != ""){
                ref = database.getReference("/Posts").orderByChild("postType").equalTo(postType)
            }
            if(!args.getString("keywords").isNullOrBlank()){
                keywords = args.getString("keywords").toString().toLowerCase(Locale.ROOT).split(" ")
            }

        }


        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val postsData: ArrayList<Post>? = createOrderedList(dataSnapshot)
                // ...
                if(postsData != null) {

                    if(!keywords.isNullOrEmpty()){
                        Log.d("keywords is not empty", keywords.toString())
                        var filteredList = ArrayList<Post>()
                        for(post in postsData){
                            if(post.getDescription().toLowerCase(Locale.ROOT).split(" ").intersect(keywords).isNotEmpty() || post.getTitle().split(" ").intersect(keywords).isNotEmpty() ){
                                filteredList.add(0, post)
                            }
                        }
                        posts.value = filteredList
                    }else{

                        posts.value = postsData
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        ref.addValueEventListener(listener)
    }

    fun getPosts(): MutableLiveData<ArrayList<Post>> {
        return posts
    }

    private fun createPost(ds: DataSnapshot): Post {
        val postType = ds.child("postType").value.toString()
        val title = ds.child("title").value.toString()
        val description = ds.child("description").value.toString()
        val price = ds.child("price").value.toString()
        val email = ds.child("email").value.toString()
        val phone = ds.child("phone").value.toString()
        val uid = ds.child("uid").value.toString()
        return Post(postType, title, description, price, email, phone, uid)
    }

    private fun createOrderedList(dataSnapshot: DataSnapshot): ArrayList<Post>? {
        var arr = ArrayList<Post>()
        for (ds in dataSnapshot.children) {
            arr.add(0, createPost(ds))
        }
        return arr
    }


    fun getPreferredComm(
        uid: String,
        view: Button,
        onClickEmail: (email: String, title: String) -> Unit,
        onClickPhone: (phone: String, title: String) -> Unit,
        email: String,
        phone: String,
        title: String
    ){
        val ref = database.getReference("/PreferredComm/$uid")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val preferredComm = dataSnapshot.getValue<String>()

                // ...
                if(preferredComm != null) {
                    if(preferredComm == "Email"){
                        view.setOnClickListener{onClickEmail(email, title)}
                    }
                    else if(preferredComm == "Phone"){
                        view.setOnClickListener{onClickPhone(phone, title)}
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        ref.addValueEventListener(listener)
    }


}
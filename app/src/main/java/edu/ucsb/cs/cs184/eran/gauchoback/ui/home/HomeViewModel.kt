package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.eran.gauchoback.ui.post.PostViewModel.Post
import java.util.HashMap

class HomeViewModel : ViewModel() {
    private var database = Firebase.database
    private var posts = MutableLiveData<HashMap<String, Post>>()

    fun setPosts(args: Bundle?){
        var postType = ""
        var keywords = listOf<String>()
        var ref = database.getReference("/Posts").orderByChild("date")
        if(args != null){
            Log.d("TAG","Args not null")
            postType = args.getString("postType").toString()
            if(postType != ""){
                ref = database.getReference("/Posts").orderByChild("postType").equalTo(postType)
            }
            if(!args.getString("keywords").isNullOrBlank()){
                keywords = args.getString("keywords").toString().split(" ")
            }

        }


        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val postsData: HashMap<String, Post>? = dataSnapshot.getValue<HashMap<String, Post>>()
                // ...
                if(postsData != null) {
                    if(!keywords.isNullOrEmpty()){
                        Log.d("keywords is not empty",keywords.toString())
                        var filteredMap = HashMap<String, Post>()
                        for(key in postsData.keys){
                            if(postsData[key]!!.getDescription().split(" ").intersect(keywords).isNotEmpty() || postsData[key]!!.getTitle().split(" ").intersect(keywords).isNotEmpty() ){
                                filteredMap[key] = postsData[key] as Post
                            }
                        }
                        posts.value = filteredMap
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

    fun getPosts(): MutableLiveData<HashMap<String, Post>> {
        return posts
    }

    fun getPreferredComm(
        uid: String,
        view: Button,
        onClickEmail: (email: String, title:String) -> Unit,
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
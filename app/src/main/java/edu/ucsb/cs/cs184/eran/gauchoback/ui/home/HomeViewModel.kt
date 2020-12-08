package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

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
import java.util.HashMap
import kotlin.reflect.KFunction2

class HomeViewModel : ViewModel() {
    private var database = Firebase.database
    private var posts = MutableLiveData<HashMap<String, Post>>()

    fun setPosts(){
        val ref = database.getReference("/Posts")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val postsData: HashMap<String, Post>? = dataSnapshot.getValue<HashMap<String, Post>>()
                // ...
                if(postsData != null) {
                    posts.value = postsData
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
package edu.ucsb.cs.cs184.eran.gauchoback.ui.profile

import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.eran.gauchoback.ui.post.PostViewModel.Post
import java.util.HashMap
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity.Companion.USER

class ProfileViewModel : ViewModel() {
    private var user = USER
    private var database = Firebase.database
    private var posts = MutableLiveData<HashMap<String, Post>>()


    fun getEmail(): String? {
        return user.getEmail()
    }

    fun setName(view: TextInputEditText) {
        view.setText(user.getName())
    }

    fun setPhone(view: TextInputEditText) {
        view.setText(user.getPhone())
    }

    fun setPreferredComm(view: AutoCompleteTextView) {
        view.setText(user.getPreferredComm(), false)
    }

    fun pushToDB(name: String, email: String, phone: String, preferredComm: String){
        user.setName(name)
        user.setPhone(phone)
        user.setPreferredComm(preferredComm)
        var ref = database.getReference("/Names/" + user.getUid())
        ref.setValue(name)
        ref = database.getReference("/Emails/" + user.getUid())
        ref.setValue(email)
        ref = database.getReference("/Phones/" + user.getUid())
        ref.setValue(phone)
        ref = database.getReference("/PreferredComm/" + user.getUid())
        ref.setValue(preferredComm)
    }

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun setMyPosts() {
        val ref = database.getReference("/Posts").orderByChild("uid").equalTo(user.getUid())
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

    fun getMyPosts(): MutableLiveData<HashMap<String, Post>> {
        return posts
    }

    fun deletePost(postID: String){
        val ref = database.getReference("/Posts/$postID")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                dataSnapshot.ref.removeValue()
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
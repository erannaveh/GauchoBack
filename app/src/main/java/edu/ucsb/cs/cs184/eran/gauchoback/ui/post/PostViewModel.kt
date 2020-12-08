package edu.ucsb.cs.cs184.eran.gauchoback.ui.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity.Companion.USER

class PostViewModel : ViewModel() {
    private var user = USER
    private var database = Firebase.database

    fun getCurrentUser(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl: Uri? = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid
        }
    }

    class Post{
        private lateinit var postType: String
        private lateinit var title: String
        private lateinit var description: String
        private lateinit var price: String
        private lateinit var email: String
        private lateinit var phone: String
        private lateinit var uid: String

        constructor()

        constructor(postType: String, title: String, description: String, price: String, email: String, phone: String, uid: String){
            this.postType = postType
            this.title = title
            this.description = description
            this.price = price
            this.email = email
            this.phone = phone
            this.uid = uid
        }

        fun getPostType(): String {
            return postType
        }

        fun getTitle(): String {
            return title
        }

        fun getDescription(): String {
            return description
        }

        fun getPrice(): String {
            return price
        }

        fun getEmail(): String {
            return email
        }

        fun getPhone(): String {
            return phone
        }

        fun getUid(): String {
            return uid
        }

    }

    fun pushPostToDB(postType: String, title: String, description: String, price: String){

        var ref = database.getReference("/Posts")
        var newPostRef = ref.push()
        newPostRef.setValue(Post(postType, title, description, price, user.getEmail(), user.getPhone(), user.getUid()))
    }
}
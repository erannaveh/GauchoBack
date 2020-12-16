package edu.ucsb.cs.cs184.eran.gauchoback.ui.post

import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity.Companion.USER

class PostViewModel : ViewModel() {
    private var user = USER
    private var database = Firebase.database

    class Post{
        private lateinit var postType: String
        private lateinit var title: String
        private lateinit var description: String
        private lateinit var price: String
        private lateinit var email: String
        private lateinit var phone: String
        private lateinit var uid: String
        private var date: Long = System.currentTimeMillis()

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

        fun getDate(): Long {
            return date
        }

    }

    fun pushPostToDB(postType: String, title: String, description: String, price: String){

        var ref = database.getReference("/Posts")
        var newPostRef = ref.push()
        newPostRef.setValue(Post(postType, title, description, price, user.getEmail(), user.getPhone(), user.getUid()))
    }
}
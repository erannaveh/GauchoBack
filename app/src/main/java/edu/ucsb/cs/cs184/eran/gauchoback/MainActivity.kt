package edu.ucsb.cs.cs184.eran.gauchoback

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var database = Firebase.database
    private var loggedInUser = FirebaseAuth.getInstance().currentUser?.uid



    class User{
        private var name: String
        private var email: String
        private var phone: String
        private var preferredComm: String
        private var uid: String

        constructor(){
            name = ""
            email = ""
            phone = ""
            preferredComm = ""
            uid = "" //FirebaseAuth.getInstance().currentUser!!.uid
        }

        fun getName(): String {
            return name
        }

        fun setName(name:String){
            this.name = name
        }

        fun getEmail(): String {
            return email
        }

        fun setEmail(email: String){
            this.email = email
        }

        fun getPhone(): String {
            return phone
        }

        fun setPhone(phone: String){
            this.phone = phone
        }

        fun getPreferredComm(): String {
            return preferredComm
        }

        fun setPreferredComm(preferredComm: String){
            this.preferredComm  = preferredComm
        }

        fun setUid(uid: String){
            this.uid = uid
        }

        fun getUid(): String {
            return uid
        }

    }

    companion object{
        var USER = User()
        private fun initializeUser(mainActivity: MainActivity){
            mainActivity.setName()
            mainActivity.setEmail()
            mainActivity.setPhone()
            mainActivity.setPreferredComm()
        }
        fun updateUser(mainActivity: MainActivity, uid: String){
            USER.setUid(uid)
            initializeUser(mainActivity)
        }
        fun updateUserSignUp(uid: String, email: String, name: String){
            USER.setUid(uid)
            USER.setEmail(email)
            USER.setName(name)
            USER.setPreferredComm("Email")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_landing_page, R.id.navigation_signup, R.id.navigation_login, R.id.navigation_home, R.id.navigation_post, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        mAuth = FirebaseAuth.getInstance()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        if(loggedInUser != null){
            updateUser(this, loggedInUser!!)
        }

    }

    private fun setName() {
        val ref = database.getReference("/Names/" + USER.getUid())
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val name = dataSnapshot.getValue<String>()
                // ...
                if(name != null) {
                    Companion.USER.setName(name)
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

    private fun setEmail() {
        val ref = database.getReference("/Emails/" + USER.getUid())
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val email = dataSnapshot.getValue<String>()
                // ...
                if(email != null) {
                    Companion.USER.setEmail(email)
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

    private fun setPhone() {
        Log.d("phone","setphone called")
        val ref = database.getReference("/Phones/" + USER.getUid())
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val phone = dataSnapshot.getValue<String>()
                // ...
                if(phone != null) {
                    Companion.USER.setPhone(phone)
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

    private fun setPreferredComm() {
        val ref = database.getReference("/PreferredComm/" + USER.getUid())
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val preferredComm = dataSnapshot.getValue<String>()
                // ...
                if(preferredComm != null) {
                    Companion.USER.setPreferredComm(preferredComm)
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
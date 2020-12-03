package edu.ucsb.cs.cs184.eran.gauchoback.ui.profile

import android.net.Uri
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {
    private lateinit var user: FirebaseUser
    private var database = Firebase.database


    fun setCurrentUser(){
        user = FirebaseAuth.getInstance().currentUser!!
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

    fun getEmail(): String? {
        return user.email
    }

    fun setName(view: TextInputEditText) {
        val ref = database.getReference("/Names/" + user.uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val name = dataSnapshot.getValue<String>()
                // ...
                if(name != null) {
                    view.setText(name.toString())
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

    fun setPhone(view: TextInputEditText) {
        val ref = database.getReference("/Phones/" + user.uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val phone = dataSnapshot.getValue<String>()
                // ...
                if(phone != null) {
                    view.setText(phone.toString())
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

    fun setPreferredComm(view: AutoCompleteTextView) {
        val ref = database.getReference("/PreferredComm/" + user.uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val preferredComm = dataSnapshot.getValue<String>()
                // ...
                if(preferredComm != null) {
                    view.setText(preferredComm.toString(), false)
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

    fun pushToDB(name: String, email: String, phone: String, preferredComm: String){
        var ref = database.getReference("/Names/" + user.uid)
        ref.setValue(name)
        ref = database.getReference("/Emails/" + user.uid)
        ref.setValue(email)
        ref = database.getReference("/Phones/" + user.uid)
        ref.setValue(phone)
        ref = database.getReference("/PreferredComm/" + user.uid)
        ref.setValue(preferredComm)
    }
}
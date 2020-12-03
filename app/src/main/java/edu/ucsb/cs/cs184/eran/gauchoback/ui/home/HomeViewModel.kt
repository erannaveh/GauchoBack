package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    private var database = Firebase.database

    fun setPosts(view: View){
        val ref = database.getReference("/Posts")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val name = dataSnapshot.getValue<ArrayList<String>>()
                // ...
                if(name != null) {
                    //view.setText(name.toString())
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
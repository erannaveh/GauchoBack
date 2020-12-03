package edu.ucsb.cs.cs184.eran.gauchoback.ui.signup

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpViewModel : ViewModel() {
    private lateinit var mAuth: FirebaseAuth
    private var database = Firebase.database

    fun setAuth(auth: FirebaseAuth){
        mAuth = auth
    }
    fun createAccount(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    fun pushNameToDB(uid: String, name: String){
        val ref = database.getReference("/Names/$uid")
        ref.setValue(name)
    }
}
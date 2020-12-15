package edu.ucsb.cs.cs184.eran.gauchoback.ui.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LogInViewModel : ViewModel() {
    private lateinit var mAuth: FirebaseAuth

    fun setAuth(auth: FirebaseAuth){
        mAuth = auth
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

}
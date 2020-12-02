package edu.ucsb.cs.cs184.eran.gauchoback.ui.signup

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    private lateinit var mAuth: FirebaseAuth

    fun createAccount(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }
}
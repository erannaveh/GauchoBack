package edu.ucsb.cs.cs184.eran.gauchoback.ui.signup

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity

class SignUpViewModel : ViewModel() {
    private lateinit var mAuth: FirebaseAuth
    private var database = Firebase.database

    fun setAuth(auth: FirebaseAuth){
        mAuth = auth
    }
    fun createAccount(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }


    fun pushToDB(uid:String, name: String, email: String, phone: String){
        MainActivity.USER.setName(name)
        MainActivity.USER.setPreferredComm("Email")
        MainActivity.USER.setPhone(phone)
        var ref = database.getReference("/Names/$uid")
        ref.setValue(name)
        ref = database.getReference("/Emails/$uid")
        ref.setValue(email)
        ref = database.getReference("/PreferredComm/$uid")
        ref.setValue("Email")
        ref = database.getReference("/Phones/$uid")
        ref.setValue(phone)
    }
}
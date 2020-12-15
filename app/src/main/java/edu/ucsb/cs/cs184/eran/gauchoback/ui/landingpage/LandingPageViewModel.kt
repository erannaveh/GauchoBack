package edu.ucsb.cs.cs184.eran.gauchoback.ui.landingpage

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LandingPageViewModel : ViewModel() {
    fun isLoggedIn():Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        val isEmailVerified = user?.isEmailVerified
        return user != null && isEmailVerified==true
    }
}
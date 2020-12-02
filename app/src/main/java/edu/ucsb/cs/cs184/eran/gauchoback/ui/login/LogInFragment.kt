package edu.ucsb.cs.cs184.eran.gauchoback.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.ucsb.cs.cs184.eran.gauchoback.R

class LogInFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        var currentUser: FirebaseUser? = mAuth.currentUser
        // use current user to update UI

    }

    private fun signIn(email: String, password: String){
        viewModel.signIn(email, password).addOnCompleteListener(requireActivity(),
            OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    val user = mAuth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    //updateUI(null)
                }

                // ...
            })
    }

}
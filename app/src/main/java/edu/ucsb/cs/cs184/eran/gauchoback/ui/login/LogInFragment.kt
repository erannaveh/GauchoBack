package edu.ucsb.cs.cs184.eran.gauchoback.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.ucsb.cs.cs184.eran.gauchoback.R

class LogInFragment : Fragment() {

    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_log_in, container, false)
        val btn = root.findViewById<Button>(R.id.logInButton)
        btn.setOnClickListener{signIn()}
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        viewModel.setAuth(mAuth)
        navController = this.findNavController()

    }

    override fun onStart() {
        super.onStart()
        var currentUser: FirebaseUser? = mAuth.currentUser
        // use current user to update UI


    }

    private fun signIn(){
        // TODO: add validation
        val email = root.findViewById<TextInputEditText>(R.id.logInEmailText).text.toString()
        val password = root.findViewById<TextInputEditText>(R.id.logInPasswordText).text.toString()
        viewModel.signIn(email, password).addOnCompleteListener(requireActivity(),
            OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    navController.navigate(R.id.action_navigation_login_to_navigation_home)
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
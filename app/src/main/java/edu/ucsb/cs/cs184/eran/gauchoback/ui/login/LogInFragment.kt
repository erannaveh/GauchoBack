package edu.ucsb.cs.cs184.eran.gauchoback.ui.login

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity
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

        val logo = root.findViewById<TextView>(R.id.gauchoback_login)
        val word: Spannable = SpannableString("Gaucho")

        word.setSpan(
            ForegroundColorSpan(Color.rgb(254,188,17)),
            0,
            word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        logo.text = word
        val wordTwo: Spannable = SpannableString("Back")

        wordTwo.setSpan(
            ForegroundColorSpan(Color.rgb(0,54,96)),
            0,
            wordTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        logo.append(wordTwo)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        viewModel.setAuth(mAuth)
        navController = this.findNavController()

    }


    private fun isValidEmail(): Boolean{
        val email = root.findViewById<TextInputEditText>(R.id.logInEmailText)
        val emailText = email.text.toString()

        if(emailText.isEmpty()){
            email.error = "Email can't be empty."
            return false
        }

        return true
    }

    private fun isValidPassword(): Boolean{
        val password = root.findViewById<TextInputEditText>(R.id.logInPasswordText)
        val passwordText = password.text.toString()

        if(passwordText.isEmpty()){
            password.error = "Password can't be empty."
            return false
        }

        return true
    }

    private fun validate(): Boolean{
        var error = false

        if(!isValidEmail()) {
            error = true
        }
        if(!isValidPassword()){
            error = true
        }

        return !error
    }

    private fun signIn() {
        // TODO: add validation

        if (validate()) {
            val email = root.findViewById<TextInputEditText>(R.id.logInEmailText).text.toString()
            val password =
                root.findViewById<TextInputEditText>(R.id.logInPasswordText).text.toString()
            Log.d("email", email)
            viewModel.signIn(email, password).addOnCompleteListener(requireActivity(),
                OnCompleteListener<AuthResult?> { task ->
                    val user = mAuth.currentUser

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                            if(user!!.isEmailVerified){
                                navController.navigate(R.id.action_navigation_login_to_navigation_home)
                                Log.d("TAG", "signInWithEmail:success")

                                MainActivity.updateUser(activity as MainActivity, user.uid)
                                //updateUI(user)
                            }else{
                                val builder : AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                                builder.setTitle("Verify Account")
                                builder.setMessage("This account has not been verified. Check ${user.email} for verification instructions.")
                                builder.setPositiveButton("OK",null)
                                builder.show()
                            }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)

                        val builder : AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle("LOGIN ERROR")
                        builder.setMessage("The email and password you entered did not match our records. Please double-check and try again.")
                        builder.setPositiveButton("OK",null)
                        builder.show()
                        //updateUI(null)
                    }

                    // ...
                })
        }
    }

}
package edu.ucsb.cs.cs184.eran.gauchoback.ui.signup

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.ucsb.cs.cs184.eran.gauchoback.MainActivity
import edu.ucsb.cs.cs184.eran.gauchoback.R
import java.util.regex.Pattern

class SignUpFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private lateinit var navController: NavController
    private lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val btn = root.findViewById<Button>(R.id.signUpButton)
        btn.setOnClickListener{createAccount()}

        val logo = root.findViewById<TextView>(R.id.gauchoback_signUp)
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
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        viewModel.setAuth(mAuth)
        navController = this.findNavController()

    }

    private fun isValidName(): Boolean{

        val name = root.findViewById<TextInputEditText>(R.id.signUpNameText)
        val nameText = name.text.toString()

        var regex = "[a-zA-Z\\s]+"
        var m: Boolean = Pattern.matches(regex, nameText)


        if(nameText.isEmpty()){
            name.error = "Name can't be empty."
            return false
        }else if(!m){
            name.error = "Enter valid name (no special characters or numbers)"
            return false
        }else{
            name.error = "Enter valid name (no special characters or numbers)"
        }

        return true
    }

    private fun isValidEmail(): Boolean{
        val email = root.findViewById<TextInputEditText>(R.id.signUpEmailText)
        val emailText = email.text.toString()

        if(emailText.isEmpty()){
            email.error = "Email can't be empty."
            return false
        }

        return true
    }

    private fun isValidPassword(): Boolean{
        val password = root.findViewById<TextInputEditText>(R.id.signUpPasswordText)
        val passwordText = password.text.toString()

        if(passwordText.isEmpty()){
            password.error = "Password can't be empty."
            return false
        }

        return true
    }

    private fun validate(): Boolean{
        var error = false

        if(!isValidName()){
            error = true
        }
        if(!isValidEmail()) {
            error = true
        }
        if(!isValidPassword()){
            error = true
        }
        return !error
    }

    private fun createAccount(){
        // TODO: add validation
        if(validate()) {
            val name = root.findViewById<TextInputEditText>(R.id.signUpNameText).text.toString()
            val email = root.findViewById<TextInputEditText>(R.id.signUpEmailText).text.toString()
            val password =
                root.findViewById<TextInputEditText>(R.id.signUpPasswordText).text.toString()
            viewModel.createAccount(email, password).addOnCompleteListener(requireActivity(),
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Log.d("TAG", "createUserWithEmail:success")
                        val user: FirebaseUser? = mAuth.currentUser
                        viewModel.pushToDB(user!!.uid, name, email)
                        MainActivity.updateUserSignUp(user.uid, email, name)
                        navController.navigate(R.id.action_navigation_signup_to_navigation_home)
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        //updateUI(null)
                    }

                    // ...
                })
        }
    }



}
package edu.ucsb.cs.cs184.eran.gauchoback.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import edu.ucsb.cs.cs184.eran.gauchoback.R
import java.util.regex.Pattern


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var root: View
    private lateinit var navController: NavController
    private lateinit var confirmDeleteDialog: AlertDialog.Builder

    companion object {
        val TAG = ProfileFragment::class.simpleName
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "on create view")
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.setMyPosts()
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        navController = this.findNavController()
        root.findViewById<Button>(R.id.saveButton).setOnClickListener{pushToDB()}
        root.findViewById<Button>(R.id.logOut).setOnClickListener{logOut()}

        populateData()
        initiateDropdown()

        profileViewModel.getMyPosts().observe(viewLifecycleOwner, Observer { it ->

            val layout = root.findViewById<LinearLayout>(R.id.myPostsLayout)
            val posts = it.values.toList()
            val keys = it.keys.toList()

            Log.d("Posts", posts.toString())
            Log.d("Keys", keys.toString())
            for (i in posts.indices) {
                val postLayout = inflater.inflate(R.layout.my_post_layout, layout, false)
                val postTitle = postLayout.findViewById<TextView>(R.id.myPostTitle)
                val postButton = postLayout.findViewById<ImageButton>(R.id.deletePost)
                postTitle.text = posts[i].getTitle()
                postButton.setOnClickListener {
                    Log.d(TAG, "dialog is visible")
                    confirmDeleteDialog = AlertDialog.Builder(this.context)
                    confirmDeleteDialog.setTitle("Confirm")
                            .setMessage("Are you sure you want to delete the post?")
                            .setPositiveButton("YES") { confirmDeleteDialog, whichButton ->
                                profileViewModel.deletePost(keys[i], it, layout)
                            }
                            .setNegativeButton("NO") { confirmDeleteDialog, whichButton ->
                                confirmDeleteDialog.dismiss()
                            }
                    confirmDeleteDialog.show()
                }
                layout.addView(postLayout)
            }
        })
        return root
    }


    private fun populateData(){
        val email: String? = profileViewModel.getEmail()
        val emailText = root.findViewById<TextInputEditText>(R.id.emailText)
        val phoneText = root.findViewById<TextInputEditText>(R.id.phoneText)
        emailText.setText(email)
        emailText.keyListener = null
        emailText.isEnabled = false
        phoneText.keyListener = null
        phoneText.isEnabled = false
        profileViewModel.setName(root.findViewById(R.id.nameText))
        profileViewModel.setPhone(phoneText)
        profileViewModel.setPreferredComm(root.findViewById(R.id.preferredCommDropdown))
    }

    private fun initiateDropdown(){
        val OPTIONS = arrayOf("Email", "Phone")

        val adapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                OPTIONS
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView = root.findViewById(R.id.preferredCommDropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)
    }

    private fun isValidName(): Boolean{

        val name = root.findViewById<TextInputEditText>(R.id.nameText)
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
            name.error = null
        }

        return true
    }

    private fun isValidPhone(): Boolean{
        val phone = root.findViewById<TextInputEditText>(R.id.phoneText)
        val phoneText = phone.text.toString()

        if(phoneText.isEmpty()){
            phone.error = "Phone Number can't be empty."
            return false
        }else if(!Patterns.PHONE.matcher(phoneText).matches()){
            phone.error = "Please enter valid phone number"
            return false
        }else{
            phone.error = null
        }
        return true
    }

    private fun isValidComm(): Boolean{

        val prefComm = root.findViewById<AutoCompleteTextView>(R.id.preferredCommDropdown)
        val prefCommText = prefComm.text.toString()

        if(prefCommText.isEmpty()) {
            prefComm.error = "Must selected preferred communication."
            return false
        }else{
            prefComm.error = null
        }
        return true
    }

    private fun validate(): Boolean{
        var error = false

        if(!isValidName()){
            error = true
        }
        if(!isValidPhone()) {
            error = true
        }
        if(!isValidComm()){
            error = true
        }
        return !error
    }

    private fun pushToDB(){
        // TODO: add validation
        if(validate()) {
            val name = root.findViewById<TextInputEditText>(R.id.nameText).text.toString()
            val email = root.findViewById<TextInputEditText>(R.id.emailText).text.toString()
            val phone = root.findViewById<TextInputEditText>(R.id.phoneText).text.toString()
            val preferredComm =
                root.findViewById<AutoCompleteTextView>(R.id.preferredCommDropdown).text.toString()
            profileViewModel.pushToDB(name, email, phone, preferredComm)
        }
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun logOut(){
        profileViewModel.logOut()
        navController.navigate(R.id.action_navigation_profile_to_navigation_landing_page)
    }

}
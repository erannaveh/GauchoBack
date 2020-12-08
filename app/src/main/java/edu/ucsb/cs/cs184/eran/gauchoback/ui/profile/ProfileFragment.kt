package edu.ucsb.cs.cs184.eran.gauchoback.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import edu.ucsb.cs.cs184.eran.gauchoback.R


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var root: View
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.setMyPosts()
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        navController = this.findNavController()
        populateData()
        initiateDropdown()
        root.findViewById<Button>(R.id.saveButton).setOnClickListener{pushToDB()}
        root.findViewById<Button>(R.id.logOut).setOnClickListener{logOut()}
        profileViewModel.getMyPosts().observe(viewLifecycleOwner, Observer { it ->
            val layout = root.findViewById<LinearLayout>(R.id.myPostsLayout)
            val posts = it.values.toList()
            val keys = it.keys.toList()
            Log.d("Posts", posts.toString())
            for (i in posts.indices) {
                val postLayout = inflater.inflate(R.layout.my_post_layout, layout, false)
                val postTitle = postLayout.findViewById<TextView>(R.id.myPostTitle)
                val postButton = postLayout.findViewById<ImageButton>(R.id.deletePost)
                postTitle.text = posts[i].getTitle()
                postButton.setOnClickListener {
                    profileViewModel.deletePost(keys[i])

                }
                layout.addView(postLayout)
            }
        })
        return root
    }

    private fun populateData(){
        val email: String? = profileViewModel.getEmail()
        if(email != null){
            val emailText = root.findViewById<TextInputEditText>(R.id.emailText)
            emailText.setText(email)
            emailText.keyListener = null
            emailText.isEnabled = false
        }
        profileViewModel.setName(root.findViewById(R.id.nameText))
        profileViewModel.setPhone(root.findViewById(R.id.phoneText))
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


    private fun pushToDB(){
        // TODO: add validation
        val name = root.findViewById<TextInputEditText>(R.id.nameText).text.toString()
        val email = root.findViewById<TextInputEditText>(R.id.emailText).text.toString()
        val phone = root.findViewById<TextInputEditText>(R.id.phoneText).text.toString()
        val preferredComm = root.findViewById<AutoCompleteTextView>(R.id.preferredCommDropdown).text.toString()
        profileViewModel.pushToDB(name, email, phone, preferredComm)
    }

    private fun logOut(){
        profileViewModel.logOut()
        navController.navigate(R.id.action_navigation_profile_to_navigation_landing_page)

    }
}
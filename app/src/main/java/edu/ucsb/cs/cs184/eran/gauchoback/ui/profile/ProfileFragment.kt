package edu.ucsb.cs.cs184.eran.gauchoback.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import edu.ucsb.cs.cs184.eran.gauchoback.R


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.setCurrentUser()
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        populateData()
        initiateDropdown()
        root.findViewById<Button>(R.id.saveButton).setOnClickListener{pushToDB()}
        return root
    }

    private fun populateData(){
        val email: String? = profileViewModel.getEmail()
        if(email != null){
            val emailText = root.findViewById<TextInputEditText>(R.id.emailText)
            emailText.setText(email)
        }
        profileViewModel.setName(root.findViewById(R.id.nameText))
        profileViewModel.setPhone(root.findViewById(R.id.phoneText))
        profileViewModel.setPreferredComm(root.findViewById(R.id.preferredCommDropdown))
    }

    private fun initiateDropdown(){
        val OPTIONS = arrayOf("Email","Phone")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            OPTIONS
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView = root.findViewById(R.id.preferredCommDropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)
    }


    fun pushToDB(){
        // TODO: add validation
        val name = root.findViewById<TextInputEditText>(R.id.nameText).text.toString()
        val email = root.findViewById<TextInputEditText>(R.id.emailText).text.toString()
        val phone = root.findViewById<TextInputEditText>(R.id.phoneText).text.toString()
        val preferredComm = root.findViewById<AutoCompleteTextView>(R.id.preferredCommDropdown).text.toString()
        profileViewModel.pushToDB(name,email,phone,preferredComm)


    }
}
package edu.ucsb.cs.cs184.eran.gauchoback.ui.post

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import edu.ucsb.cs.cs184.eran.gauchoback.R

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private lateinit var imageUploadView: ImageView
    private var PICK_IMAGE = 1

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var dropDownText: AutoCompleteTextView
    private lateinit var root: View
    private lateinit var navController: NavController

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        postViewModel =
                ViewModelProvider(this).get(PostViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_post, container, false)
        navController = this.findNavController()
        // Set up Post Type Dropdown Menu
        initiateDropdown()

        // Set up On-Click Listener for the Image Upload
        initiateImageUpload()

        val postButton = root.findViewById<Button>(R.id.new_post_submit_button)
        postButton.setOnClickListener{pushPostToDB()}

        return root
    }

    // In progress
    // https://stackoverflow.com/questions/25182838/how-to-upload-image-from-gallery-using-fragment
    fun captureImage() {
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    }

    private fun initiateDropdown(){
        textInputLayout = root.findViewById(R.id.new_post_type_dropdown)
        dropDownText = root.findViewById(R.id.post_dropdown)
        val postTypes = listOf("Selling", "ISO", "Housing", "Misc")
        val adapter = ArrayAdapter(requireContext(), R.layout.post_type_dropdown, postTypes)
        dropDownText.setAdapter(adapter)
    }

    private fun initiateImageUpload(){
        imageUploadView = root.findViewById<ImageView>(R.id.new_post_image_upload)
        imageUploadView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val gallery: Intent = Intent()
                gallery.type = "image/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(gallery, "Complete Action using"), 1)
            }
        })
    }

    private fun pushPostToDB(){
        // TODO: add validation
        val postType = root.findViewById<AutoCompleteTextView>(R.id.post_dropdown).text.toString()
        val title = root.findViewById<TextInputEditText>(R.id.new_post_title_text).text.toString()
        val description = root.findViewById<TextInputEditText>(R.id.new_post_description_text).text.toString()
        val price = root.findViewById<TextInputEditText>(R.id.new_post_price_text).text.toString()
        postViewModel.pushPostToDB(postType, title, description, price)
        navController.navigate(R.id.action_navigation_post_to_navigation_home)
    }
}
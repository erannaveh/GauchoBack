package edu.ucsb.cs.cs184.eran.gauchoback.ui.post

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import edu.ucsb.cs.cs184.eran.gauchoback.R

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private lateinit var imageUploadView: ImageView
    private var PICK_IMAGE = 1

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var dropDownText: AutoCompleteTextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        postViewModel =
                ViewModelProvider(this).get(PostViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_post, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Post Type Dropdown Menu
        textInputLayout = requireActivity().findViewById(R.id.new_post_type_dropdown)
        dropDownText = requireActivity().findViewById(R.id.post_dropdown)
        val postTypes = listOf("Selling", "ISO", "Housing", "Misc")
        val adapter = ArrayAdapter(requireContext(), R.layout.post_type_dropdown, postTypes)
        dropDownText.setAdapter(adapter)

        // Set up On-Click Listener for the Image Upload
        imageUploadView = requireActivity().findViewById<ImageView>(R.id.new_post_image_upload)
        imageUploadView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val gallery: Intent = Intent()
                gallery.setType("image/*")
                gallery.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(gallery, "Complete Action using"), 1)
            }
        })
    }

    // In progress
    // https://stackoverflow.com/questions/25182838/how-to-upload-image-from-gallery-using-fragment
    fun captureImage() {
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    }
}
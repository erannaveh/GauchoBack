package edu.ucsb.cs.cs184.eran.gauchoback.ui.post

import android.R.attr
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import edu.ucsb.cs.cs184.eran.gauchoback.R
import java.util.jar.Manifest


class PostFragment : Fragment() {

    companion object {
        val TAG = PostFragment::class.simpleName
        private val IMAGE_PICK_CODE = 1000
        private val IMAGE_CAPTURE_CODE = 1001
    }

    private lateinit var postViewModel: PostViewModel
    private lateinit var imageUploadView: ImageView

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var dropDownText: AutoCompleteTextView
    private lateinit var root: View
    private lateinit var navController: NavController

    private lateinit var selectedImage: Bitmap // push this to DB

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

    private fun initiateDropdown(){
        textInputLayout = root.findViewById(R.id.new_post_type_dropdown)
        dropDownText = root.findViewById(R.id.post_dropdown)
        val postTypes = listOf("Selling", "ISO", "Housing", "Misc")
        val adapter = ArrayAdapter(requireContext(), R.layout.post_type_dropdown, postTypes)
        dropDownText.setAdapter(adapter)
    }

    private fun initiateImageUpload(){
        imageUploadView = root.findViewById<ImageView>(R.id.new_post_image_upload)
        imageUploadView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                selectImage()
            }
        })
    }

    private fun isValidTitle(): Boolean{
        val title = root.findViewById<TextInputEditText>(R.id.new_post_title_text)
        val titleText = title.text.toString()

        if(titleText.isEmpty()){
            title.error = "Title can't be empty."
            return false
        }else{
            title.error = null
        }

        return true

    }

    private fun isValidDescr(): Boolean{
        val description = root.findViewById<TextInputEditText>(R.id.new_post_description_text)
        val descriptionText = description.text.toString()

        if(descriptionText.isEmpty()){
            description.error = "Description can't be empty."
            return false
        }else{
            description.error = null
        }
        return true
    }

    private fun isValidType(): Boolean{
        val postType = root.findViewById<AutoCompleteTextView>(R.id.post_dropdown)
        val postTypeText = postType.text.toString()

        if(postTypeText.isEmpty()){
            postType.error = "Must select post type."
            return false
        }else{
            postType.error = null
        }
        return true

    }
    private fun isValidPrice(): Boolean {
        val postType = root.findViewById<AutoCompleteTextView>(R.id.post_dropdown).text.toString()
        val price = root.findViewById<TextInputEditText>(R.id.new_post_price_text)
        val priceText = price.text.toString()

        if((postType == "Selling") && priceText.isEmpty()){
            price.error = "Price required for Selling"
            return false
        }else{
            price.error = null
        }

        return true

    }

    private fun validate(): Boolean{

        var error = false

        if(!isValidType()) {
            error = true
        }
        if(!isValidTitle()) {
            error = true
        }
        if(!isValidDescr()) {
            error = true
        }
        if(!isValidPrice()){
            error = true
        }
        return !error
    }

    private fun pushPostToDB(){
        // TODO: add validation
        if(validate()) {
            val postType =
                root.findViewById<AutoCompleteTextView>(R.id.post_dropdown).text.toString()
            val title =
                root.findViewById<TextInputEditText>(R.id.new_post_title_text).text.toString()
            val description =
                root.findViewById<TextInputEditText>(R.id.new_post_description_text).text.toString()
            val price =
                root.findViewById<TextInputEditText>(R.id.new_post_price_text).text.toString()
            postViewModel.pushPostToDB(postType, title, description, price)
            navController.navigate(R.id.action_navigation_post_to_navigation_home)
        }
    }

    fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Add Photo")
        builder.setItems(options) { dialog, item ->
            if (options[item].equals("Take Photo")) {
                captureImage()
            } else if (options[item].equals("Choose from Gallery")) {
                pickFromGallery()
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun pickFromGallery() {
        Log.d(TAG, "choosing photo from gallery")
        //val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val pickPhotoIntent = Intent()
        pickPhotoIntent.setAction(Intent.ACTION_GET_CONTENT)
        pickPhotoIntent.setType("image/*")
        startActivityForResult(pickPhotoIntent, IMAGE_PICK_CODE)
    }

    private fun captureImage() {
        val capturePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (capturePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            Log.d(TAG, "taking photo")
            startActivityForResult(capturePictureIntent, IMAGE_CAPTURE_CODE)
        } else {
            Log.d(TAG, "unable to open camera")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === Activity.RESULT_OK && data != null) {
            if (requestCode === IMAGE_CAPTURE_CODE) {
                selectedImage = data.extras?.get("data") as Bitmap
                imageUploadView.setImageBitmap(selectedImage)
            } else if (requestCode === IMAGE_PICK_CODE) {
                val imageUri = data!!.data
                selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri)
                imageUploadView.setImageBitmap(selectedImage)
            }
        }
    }
}
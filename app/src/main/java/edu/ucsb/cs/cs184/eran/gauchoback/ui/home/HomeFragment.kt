package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ucsb.cs.cs184.eran.gauchoback.R


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var navController: NavController
    private lateinit var root: View
    val Fragment.packageManager get() = activity?.packageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.setPosts()
        navController = this.findNavController()

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        val searchBtn = root.findViewById<FloatingActionButton>(R.id.searchButton)

        searchBtn.setOnClickListener{navController.navigate(R.id.action_navigation_home_to_searchFragment)}
        homeViewModel.getPosts().observe(viewLifecycleOwner, Observer { it ->
            val postsLinearLayout = root.findViewById<LinearLayout>(R.id.postsLinearLayout)
            val list = it.values.toList()
            for(post in list){
                val postLayout = inflater.inflate(R.layout.post_layout, postsLinearLayout, false)
                val postTitle = postLayout.findViewById<TextView>(R.id.postTitle)
                val postDescription = postLayout.findViewById<TextView>(R.id.postDescription)
                val postButton = postLayout.findViewById<Button>(R.id.postContactSeller)
                val postType = postLayout.findViewById<TextView>(R.id.postType)
                homeViewModel.getPreferredComm(post.getUid(), postButton, ::onClickEmail, ::onClickPhone, post.getEmail(), post.getPhone(), post.getTitle())
                postTitle.text = post.getTitle()
                postDescription.text = post.getDescription()
                postType.text = post.getPostType()
                Log.d("home post", post.getDescription())
                val paddingDp = 25
                val density = requireContext().resources.displayMetrics.density
                val paddingPixel: Int = ((paddingDp * density).toInt())
                postLayout.setPadding(0, paddingPixel, 0, paddingPixel)
                postsLinearLayout.addView(postLayout)
            }
        })

        return root
    }

    private fun onClickEmail(email: String, title: String){
        Log.d("Clicked Email", "Clicked")
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, "Responding to $title on GauchoBack")
        }
        if(packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }

    private fun onClickPhone(phone: String, title: String){
        Log.d("Clicked Phone", "Clicked")
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("smsto:$phone")  // This ensures only SMS apps respond
            //putExtra("sms_body", "Hi I'd like to reach out about $title on GauchoBack")
        }
        if(packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }

    }


}
package edu.ucsb.cs.cs184.eran.gauchoback.ui.landingpage

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.ucsb.cs.cs184.eran.gauchoback.R

class LandingPageFragment : Fragment() {

    companion object {
        fun newInstance() = LandingPageFragment()
    }

    private lateinit var viewModel: LandingPageViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_landing_page, container, false)
        val loginBtn = root.findViewById<Button>(R.id.loginLandingPage)
        val signUpBtn = root.findViewById<Button>(R.id.signUpLandingPage)
        loginBtn.setOnClickListener{onClickLogIn()}
        signUpBtn.setOnClickListener{onClickSignUp()}
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LandingPageViewModel::class.java)
        navController = this.findNavController()
        if(viewModel.isLoggedIn()){
            navController.navigate(R.id.action_navigation_landing_page_to_navigation_home)
        }
        // TODO: Use the ViewModel
    }

    fun onClickLogIn() {
        navController.navigate(R.id.action_navigation_landing_page_to_navigation_login)
    }

    fun onClickSignUp() {
        navController.navigate(R.id.action_navigation_landing_page_to_navigation_signup)
    }

}
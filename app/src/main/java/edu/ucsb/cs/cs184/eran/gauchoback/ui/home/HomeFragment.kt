package edu.ucsb.cs.cs184.eran.gauchoback.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ucsb.cs.cs184.eran.gauchoback.R
import edu.ucsb.cs.cs184.eran.gauchoback.ui.search.SearchFragment


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        val searchBtn = root.findViewById<FloatingActionButton>(R.id.searchButton)

        searchBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTransaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(view!!.id, SearchFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        })

        return root
    }


}
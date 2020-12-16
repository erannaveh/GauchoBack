package edu.ucsb.cs.cs184.eran.gauchoback.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import edu.ucsb.cs.cs184.eran.gauchoback.R

class SearchFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var root: View

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_search, container, false)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        navController = this.findNavController()
        initiateDropdown()
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        val searchButton = root.findViewById<Button>(R.id.submitSearch)
        searchButton.setOnClickListener{
            navController.navigate(R.id.action_searchFragment_to_navigation_home,createBundle())
        }
        return root
    }

    private fun createBundle(): Bundle {
        val postType = root.findViewById<AutoCompleteTextView>(R.id.searchPostTypeDropdown).text.toString()
        val keywords = root.findViewById<TextInputEditText>(R.id.searchView).text.toString()
        return bundleOf("postType" to postType, "keywords" to keywords)

    }

    private fun initiateDropdown(){
        val OPTIONS = arrayOf("Housing", "Selling", "ISO", "Misc")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            OPTIONS
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView = root.findViewById(R.id.searchPostTypeDropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)
    }

}
package edu.ucsb.cs.cs184.eran.gauchoback.ui.landingpage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.ucsb.cs.cs184.eran.gauchoback.R

class LandingPageFragment : Fragment() {

    companion object {
        fun newInstance() = LandingPageFragment()
    }

    private lateinit var viewModel: LandingPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LandingPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
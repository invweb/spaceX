package com.spacex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacex.database.entity.Launch

import com.test.spacex.R
import com.test.spacex.databinding.FragmentStartBinding

class StartFragment : Fragment(){
    private lateinit var startFragmentBinding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startFragmentBinding = FragmentStartBinding.inflate(layoutInflater)
        return startFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launch: Launch? = requireArguments().getParcelable<Launch>("launch")
        startFragmentBinding.launch = launch
        if (launch?.details.equals("empty")) {
            startFragmentBinding.detailsText.text = getString(R.string.details_is_empty)
        } else {
            startFragmentBinding.detailsText.text = launch?.details
        }
    }
}

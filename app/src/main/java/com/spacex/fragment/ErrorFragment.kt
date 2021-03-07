package com.spacex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.spacex.tech.Functions

import com.test.spacex.R
import com.test.spacex.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val errorFragmentBinding: FragmentErrorBinding =
            FragmentErrorBinding.inflate(layoutInflater)
        val viewLocallyButton = errorFragmentBinding.viewLocallyButton
        viewLocallyButton.setOnClickListener {
            Functions.saveCheck(check = false, activity = requireActivity())
            viewLocallyButton.findNavController().navigate(R.id.startListFragment)
        }
        return view
    }
}

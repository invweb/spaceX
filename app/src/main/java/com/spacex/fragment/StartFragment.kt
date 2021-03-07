package com.spacex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.spacex.database.entity.Launch

import com.test.spacex.R
import com.test.spacex.databinding.FragmentErrorBinding
import com.test.spacex.databinding.FragmentStartBinding
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment(){
    lateinit var startFragmentBinding: FragmentStartBinding

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
        if (launch?.details.equals("empty")) {
            startFragmentBinding.detailsText.text = getString(R.string.details_is_empty)
        } else {
            startFragmentBinding.detailsText.text = launch?.details
        }

        startFragmentBinding.rocketName.text = launch?.rocket?.rocketName
        startFragmentBinding.rocketType.text = launch?.rocket?.rocketName

        startFragmentBinding.coreText.text = launch?.reuse?.core.toString()
        startFragmentBinding.sideCore1Text.text = launch?.reuse?.side_core1.toString()
        startFragmentBinding.sideCore2Text.text = launch?.reuse?.side_core2.toString()
        startFragmentBinding.fairingsText.text = launch?.reuse?.fairings.toString()
        startFragmentBinding.capsuleText.text = launch?.reuse?.capsule.toString()

        startFragmentBinding.missionFlightNumberText.text = launch?.flightNumber.toString()

        Glide.with(this).load(launch?.links?.missionPatchSmall).into(mission_image)
    }
}

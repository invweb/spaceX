package com.spacex.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacex.adapters.MissionAdapter
import com.spacex.application.MyApplication
import com.spacex.database.entity.Launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.spacex.R
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.*
import com.spacex.mvvm.InitViewModel
import com.spacex.mvvm.InitViewModelFactory
import com.test.spacex.databinding.FragmentStartListBinding

class StartListFragment : Fragment() {

    private var flag : Boolean = false
    private lateinit var previousColoredView : View
    private lateinit var startListFragmentBinding : FragmentStartListBinding

    private lateinit var  viewModelInit: InitViewModel

    override fun onStart() {
        super.onStart()
        flag = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startListFragmentBinding = FragmentStartListBinding.inflate(layoutInflater)
        return startListFragmentBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app: MyApplication = requireActivity().application as MyApplication

        startListFragmentBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        startListFragmentBinding.recyclerView.setHasFixedSize(true)

        viewModelInit = InitViewModelFactory(app).create(InitViewModel::class.java)
        viewModelInit.saveLaunchesToDB(requireActivity())

        val launchesObserver = Observer<List<Launch>> { launches ->
            if(flag) {
                flag = false
                val missionAdapter = MissionAdapter(requireContext())
                missionAdapter.setLaunches(launches)
                missionAdapter.notifyDataSetChanged()
                startListFragmentBinding.recyclerView.adapter = missionAdapter
                missionAdapter.notifyDataSetChanged()

                launches?.let {
                    viewModelInit.getTakenFromDatabaseValue().observe(viewLifecycleOwner,
                        Observer { token ->
                            startListFragmentBinding.takenFromDatabaseValue.text = token.toString()
                    })

                    viewModelInit.saveDataToDbAsync(it)
                    addLaunchYearsOnScreen(app, launches, startListFragmentBinding.yearsContainer,
                        startListFragmentBinding.recyclerView)
//                    flag = false
                }
            }
        }
        viewModelInit.observeDataIdDB().observe(this.activity as LifecycleOwner, launchesObserver)
        startListFragmentBinding.dataUpdatedTime.text =
            viewModelInit.loadTimeFromSharedPreference(context).value
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addLaunchYearsOnScreen(app: MyApplication, launches: List<Launch>,
                                       yearsContainer : LinearLayout,
                                       missionsRecyclerView : RecyclerView){
        val textAll = TextView(context)
        yearsContainer.removeAllViews()
        val yearList: ArrayList<TextView> = ArrayList()

        textAll.setBackgroundColor(Color.CYAN)
        textAll.text = getString(R.string.all)
        yearsContainer.addView(textAll)
        yearList.add(textAll)
        previousColoredView = textAll

        val years: ArrayList<Int> = ArrayList()
        for (launch in launches){
            val launchYear: Int = launch.launchYear

            if (!years.contains(launchYear)){
                years.add(launchYear)
                val year = TextView(context)
                year.text = launchYear.toString()
                yearsContainer.addView(year)
                yearList.add(year)

                year.setOnClickListener {
                    val background : ColorDrawable? = it.background as ColorDrawable?
                    val backgroundColor = background?.color
                    if(backgroundColor == null){
                        previousColoredView.setBackgroundColor(Color.WHITE)
                    } else if (backgroundColor == Color.CYAN) {
                        previousColoredView.setBackgroundColor(Color.WHITE)
                    }

                    previousColoredView = it

                    it.setBackgroundColor(Color.CYAN)

                    app.database.getDao().getLaunchesWithWhere(
                        year.text.toString().toInt()).observe(viewLifecycleOwner, Observer {
                            launches ->
                                val missionAdapter = MissionAdapter(requireContext())
                                missionAdapter.setLaunches(launches)
                                missionAdapter.notifyDataSetChanged()

                                missionsRecyclerView.adapter = missionAdapter
                                missionAdapter.notifyDataSetChanged()
                    })
                }
            }
        }


        textAll.setOnClickListener { it ->
            if (this::previousColoredView.isInitialized) {
                previousColoredView.setBackgroundColor(Color.WHITE)
            }

            it.setBackgroundColor(Color.CYAN)

            app.database.getDao().getLaunches().observe(viewLifecycleOwner, Observer {
                val missionAdapter = MissionAdapter(requireContext())
                missionAdapter.setLaunches(launches)
                missionAdapter.notifyDataSetChanged()

                missionsRecyclerView.adapter = missionAdapter
                missionAdapter.notifyDataSetChanged()
            })
        }
    }
}

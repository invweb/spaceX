package com.spacex.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacex.database.entity.Launch
import com.test.spacex.BR
import com.test.spacex.R
import com.test.spacex.databinding.LaunchesBinding

class MissionAdapter(private val context: Context) : RecyclerView.Adapter<MissionAdapter.MyViewHolder>() {
    private var localLaunches: List<Launch> = emptyList()

    fun setLaunches(launches: List<Launch>){
        localLaunches = launches
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val launch : Launch = localLaunches[position]

        launch.let { holder.setLaunchItem(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: LaunchesBinding = inflate(inflater, R.layout.launches, parent,
            false)
        return MyViewHolder(binding, context)
    }

    override fun getItemCount(): Int = localLaunches.size

    class MyViewHolder(launchesBinding: LaunchesBinding, private val context: Context)
        : RecyclerView.ViewHolder(launchesBinding.root)  {

        private val binding: ViewDataBinding = launchesBinding

        fun setLaunchItem(launchItem: Launch){
            binding.setVariable(BR.launch, launchItem)

            binding.root.setOnClickListener {
                val itemBundle = Bundle()
                itemBundle.putParcelable("launch", launchItem)
                it.findNavController().navigate(R.id.startFragment, itemBundle)
            }
        }
    }
}

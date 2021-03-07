package com.spacex.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacex.database.entity.Launch
import com.test.spacex.R

class MissionAdapter(val context: Context) : RecyclerView.Adapter<MissionAdapter.MyViewHolder>() {
    private var localLaunches: List<Launch> = emptyList()

    fun setLaunches(setedLaunches: List<Launch>){
        localLaunches = setedLaunches
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val launch : Launch = localLaunches[position]
        launch.let { holder.setLaunchItem(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.launches, parent, false)
        return MyViewHolder(view, context)
    }

    override fun getItemCount(): Int = localLaunches.size

    class MyViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val textMissionFlightNameView : TextView = itemView.findViewById(R.id.mission_flight_name)
        private val textMissionFlightNumber : TextView = itemView.findViewById(R.id.mission_flight_number)
        private val textLaunchYear : TextView = itemView.findViewById(R.id.mission_launch_year)
        private val image : ImageView = itemView.findViewById(R.id.image)

        fun setLaunchItem(launchItem: Launch){
            textMissionFlightNameView.text = launchItem.missionName
            textMissionFlightNumber.text = launchItem.flightNumber.toString()
            textLaunchYear.text = launchItem.launchYear.toString()

            itemView.setOnClickListener {
                val itemBundle = Bundle()
                itemBundle.putParcelable("launch", launchItem)
                it.findNavController().navigate(R.id.startFragment, itemBundle)
            }

            Glide.with(context).load(launchItem.links.missionPatchSmall).into(image)
        }
    }
}

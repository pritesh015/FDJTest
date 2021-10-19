package com.example.fdjtest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.fdjtest.api.models.ResponseTeamsInLeagueDetails
import com.example.fdjtest.databinding.ItemsListTeamsBinding

class TeamsListAdapter(var items: List<ResponseTeamsInLeagueDetails>, val context: Context): RecyclerView.Adapter<TeamsHolder>() {

    var listener: OnClickItemListener? = null
    private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsHolder {
        val binding = ItemsListTeamsBinding.inflate(LayoutInflater.from(context), parent, false)
        return TeamsHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamsHolder, position: Int) {
        Glide.with(context)
            .load(items[position].teamBadge)
            .apply(requestOptions)
            .into(holder.ivTeam)

        holder.ivTeam.setOnClickListener {
            Toast.makeText(context, items[position].teamName, Toast.LENGTH_SHORT).show()
            items[position].teamName?.let { it1 -> listener?.onItemClicked(it1) }
        }
    }


    fun setList(list: List<ResponseTeamsInLeagueDetails>) {
        items = list
        notifyDataSetChanged()
    }

    fun addList(list: List<ResponseTeamsInLeagueDetails>) {
        items += list
        notifyDataSetChanged()
    }

    interface OnClickItemListener {
        fun onItemClicked(teamName: String)
    }

}

class TeamsHolder(binding: ItemsListTeamsBinding): RecyclerView.ViewHolder(binding.root) {
    val ivTeam =  binding.ivTeam
}
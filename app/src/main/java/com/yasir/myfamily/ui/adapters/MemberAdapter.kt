package com.yasir.myfamily.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.databinding.ItemMemberBinding
import com.yasir.myfamily.ui.dataClasses.MemberModel

class MemberAdapter(private val listMember :List<MemberModel>):RecyclerView.Adapter<MemberAdapter.ViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = ItemMemberBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val itemList = listMember[position]
        holder.userName.text = itemList.name
        holder.address.text = itemList.address
        holder.batteryPercent.text = itemList.battery
        holder.distance.text = itemList.distance

    }

    override fun getItemCount(): Int {
        return listMember.size
    }

    class ViewHolder( binding: ItemMemberBinding):RecyclerView.ViewHolder(binding.root){
        val userName = binding.name
        val address = binding.address
        val batteryPercent = binding.batteryPercent
        val distance = binding.distanceValue
    }
}
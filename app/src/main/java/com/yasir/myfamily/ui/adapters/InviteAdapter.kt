package com.yasir.myfamily.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.databinding.ItemInviteBinding
import com.yasir.myfamily.ui.dataClasses.ContactModel


class InviteAdapter(private val listContacts :List<ContactModel>):RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val viewItem = ItemInviteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val contactList = listContacts[position]
        holder.name.text = contactList.name
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }


    inner class ViewHolder( binding: ItemInviteBinding):RecyclerView.ViewHolder(binding.root){
        val name = binding.name


    }
}
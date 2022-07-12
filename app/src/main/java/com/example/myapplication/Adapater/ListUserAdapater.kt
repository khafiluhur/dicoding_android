package com.example.myapplication.Adapater

import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.Utils.loadImage
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.UserResponse
import com.example.myapplication.Utils.OnItemClickCallback
import com.example.myapplication.databinding.ItemUserBinding

class ListUserAdapater : RecyclerView.Adapter<ListUserAdapater.ListViewHolder>() {

    private var listUserResponse = ArrayList<UserResponse>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun addDataToList(items: ArrayList<UserResponse>) {
        listUserResponse.clear()
        listUserResponse.addAll(items)
        //Penerapan method notifyDatasetChanged() saat ini tidak disarankan,
        // untuk kedepannya kamu bisa menggunakan DiffUtils untuk melakukan update data pada recyclerview.
        // Sesuaikan pada class yang lainnya ya.
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUserResponse[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUserResponse[position]) }
    }

    override fun getItemCount(): Int = listUserResponse.size

    class ListViewHolder(private var itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(userResponse: UserResponse) {
            itemBinding.apply {
                nameUser.text = userResponse.login
                username.text = StringBuilder("@").append(userResponse.login)
                imgUser.loadImage(userResponse.avatarUrl)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}

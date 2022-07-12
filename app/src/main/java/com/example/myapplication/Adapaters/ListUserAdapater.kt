package com.example.myapplication.Adapaters


import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Utils.loadImage
import com.example.myapplication.Models.UserResponse
import com.example.myapplication.Utils.OnItemClickCallback
import com.example.myapplication.Utils.UserDiffUtilCallback
import com.example.myapplication.databinding.ItemUserBinding

class ListUserAdapater : RecyclerView.Adapter<ListUserAdapater.ListViewHolder>() {

    private var listUserResponse = ArrayList<UserResponse>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun addDataToList(items: ArrayList<UserResponse>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffUtilCallback(listUserResponse, items))

        listUserResponse.clear()
        listUserResponse.addAll(items)
        diffResult.dispatchUpdatesTo(this)
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

    private class DiffCallback : DiffUtil.ItemCallback<ArrayList<UserResponse>>() {

        override fun areItemsTheSame(
            oldItem: ArrayList<UserResponse>,
            newItem: ArrayList<UserResponse>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ArrayList<UserResponse>,
            newItem: ArrayList<UserResponse>
        ): Boolean {
            return oldItem == newItem
        }

    }
}
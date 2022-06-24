package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.databinding.ItemUserBinding

class ListUserAdapater(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapater.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ImageView {
        fun loadImage() { println("Class method") }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, _, avatar, _, _, _, username) = listUser[position]
        fun ImageView.loadImage(url: String?) {
            Glide.with(holder.itemView.context)
                .load(url)
                .apply(RequestOptions().override(500, 500))
                .centerCrop()
                .into(holder.itemBinding.imgUser)
        }
        // Untuk menghindari penulisan kode yang berulang, kamu bisa memanfaatkan Kotlin Extensions.
        // Sebagai contoh :
        //fun ImageView.loadImage(url: String?) { Glide.with(this.context) .load(url) .apply(RequestOptions().override(500, 500)) .centerCrop() .into(this) }
        // Cara menggunakannya seperti ini
        //ImageView.loadImage("url")
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.itemBinding.imgUser)
        holder.itemBinding.nameUser.text = name
        holder.itemBinding.username.text = StringBuilder(holder.itemView.context.getString(R.string.siput)).append(username)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root)
}

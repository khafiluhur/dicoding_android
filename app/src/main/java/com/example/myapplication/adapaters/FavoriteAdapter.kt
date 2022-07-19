package com.example.myapplication.adapaters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.Favorite
import com.example.myapplication.databinding.ItemFavoriteBinding
import com.example.myapplication.helper.FavoriteDiffUtilCallback
import com.example.myapplication.utils.OnItemFavoriteClickCallback
import com.example.myapplication.utils.loadImage
import com.example.myapplication.viewModels.FavoriteAddUpdateViewModel
import com.example.myapplication.viewModels.FavoriteViewModelFactory

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<Favorite>()
    private lateinit var onItemFavoriteClickCallback: OnItemFavoriteClickCallback

    fun setListFavorite(listFavorite: List<Favorite>) {
        val diffCallback = FavoriteDiffUtilCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
        holder.itemView.setOnClickListener { onItemFavoriteClickCallback.onItemClicked(listFavorite[position]) }
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    class FavoriteViewHolder(private val itemBinding: ItemFavoriteBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(favorite: Favorite) {
            itemBinding.apply {
                nameUser.text = favorite.login
                username.text = StringBuilder("@").append(favorite.login)
                imgUser.loadImage(favorite.url)
                btnDelete.setOnClickListener {
                    val favoriteAddUpdateViewModel = obtainViewModel(it.context as AppCompatActivity)
                    favoriteAddUpdateViewModel.delete(favorite)
                    Toast.makeText(it.context, R.string.delete, Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
            val factory = FavoriteViewModelFactory.getInstance(activity.application)
            return ViewModelProvider(activity, factory)[FavoriteAddUpdateViewModel::class.java]
        }
    }

    fun setOnItemFavoriteClickCallback(onItemFavoriteClickCallback: OnItemFavoriteClickCallback) {
        this.onItemFavoriteClickCallback = onItemFavoriteClickCallback
    }

}

package com.example.myapplication.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Models.UserResponse
import com.example.myapplication.Adapater.ListUserAdapater
import com.example.myapplication.DetailActivity
import com.example.myapplication.Utils.ApiConfig
import com.example.myapplication.Utils.OnItemClickCallback
import com.example.myapplication.databinding.FragmentFollowersBinding
import com.example.myapplication.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Response

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListUserAdapater by lazy {
        ListUserAdapater()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<UserResponse>(ARG_PARCEL)
        user?.let { setListFollowing(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setListFollowing(user: UserResponse) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowing(user.login!!)
        client.enqueue(object : retrofit2.Callback<ArrayList<UserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserResponse>>,
                response: Response<ArrayList<UserResponse>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        adapter.addDataToList(responseBody)
                        showRecyclerList()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        binding.rvUser.layoutManager = LinearLayoutManager(view?.context)
        binding.rvUser.adapter = adapter
        binding.rvUser.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                val intentDetail = Intent(context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.KEY_USER, user)
                startActivity(intentDetail)
            }
        })
    }

    companion object {
        private const val ARG_PARCEL = "user_model"

        @JvmStatic
        fun newInstance(userResponse: UserResponse) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARCEL, userResponse)
                }
            }
    }
}
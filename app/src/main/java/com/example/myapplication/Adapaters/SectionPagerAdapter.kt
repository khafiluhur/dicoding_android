package com.example.myapplication.Adapaters

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Models.UserResponse
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.Fragments.FollowersFragment
import com.example.myapplication.Fragments.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var model: UserResponse? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(model!!)
            1 -> fragment = FollowingFragment.newInstance(model!!)
        }
        return fragment as Fragment
    }

}
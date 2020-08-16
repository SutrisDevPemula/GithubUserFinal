package com.developer2t.mygithubuserfinal.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.view.fragment.FollowersFragment
import com.developer2t.mygithubuserfinal.view.fragment.FollowingsFragment
import java.lang.reflect.Array.newInstance

class SectionPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private val name: String?
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val titles = intArrayOf(
        R.string.followers,
        R.string.followings

    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowersFragment.newInstance(name)
            1 -> fragment = FollowingsFragment.newInstance(name)
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(titles[position])
    }

    override fun getCount(): Int {
        return 2
    }
}
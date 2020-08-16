package com.developer2t.mygithubuserfinal.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.adapter.CardUserAdapter
import com.developer2t.mygithubuserfinal.viewmodel.ViewModelListFollow
import kotlinx.android.synthetic.main.activity_main.*


class FollowersFragment : Fragment() {

    companion object {
        fun newInstance(name: String?) = FollowersFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
            }
        }
    }

    private lateinit var adapter: CardUserAdapter
    private lateinit var viewModelListFollow: ViewModelListFollow
    private val name by lazy { arguments?.getString("name") ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folowers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerCardView()

        viewModelListFollow =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ViewModelListFollow::class.java)
        viewModelListFollow.setFollowers(ViewModelListFollow.FOLLOWERS, name)

        viewModelListFollow.getFollowers().observe(viewLifecycleOwner, Observer { followersList ->
            if (followersList != null) {
                adapter.setData(followersList)
            }
        })
    }

    private fun showRecyclerCardView() {
        adapter = CardUserAdapter(requireContext())
        adapter.notifyDataSetChanged()

        rv_item.layoutManager = LinearLayoutManager(requireContext())
        rv_item.adapter = adapter

    }
}
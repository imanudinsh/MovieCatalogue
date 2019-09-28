package com.im.layarngaca21.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.im.layarngaca21.R
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.viewmodel.TVShowsViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_tv_show.progressBar
import kotlinx.android.synthetic.main.fragment_tv_show.rv_category


class TVShowFragment : Fragment(), ViewMessages {


    private lateinit  var tvShowsViewModel: TVShowsViewModel
    private lateinit var adapter: TVShowViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_tv_show, container, false)

        activity?.window?.setSharedElementExitTransition(TransitionInflater.from(context).inflateTransition(R.transition.element_transition))

        adapter = TVShowViewAdapter(activity!!)
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerCardView()

        tvShowsViewModel = ViewModelProviders.of(this).get(TVShowsViewModel::class.java)
        tvShowsViewModel.getTVShows().observe(this, getTV)
        tvShowsViewModel.messagesEvent.setEventReceiver(this, this)
        tvShowsViewModel.setTVShows()
        progressBar.visibility = View.VISIBLE

    }

    private val getTV = object : Observer<List<TV>?> {
        override fun onChanged(listTV: List<TV>?) {
            if (listTV != null) {
                adapter.setData(listTV)
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                Log.d("TVFragment","$listTV")
            }
        }
    }

    private fun showRecyclerCardView() {
        rv_category.layoutManager = LinearLayoutManager(context)
        rv_category.adapter = adapter
    }


    override fun showMessage(message: Int, category: String) {
        context?.let { CustomToast().show(it, resources.getString(message), category) }
        progressBar.visibility = View.GONE
        view_no_data.visibility = if(adapter.itemCount>0) View.GONE else View.VISIBLE
    }

}

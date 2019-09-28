package com.im.layarngaca21.view.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.im.layarngaca21.R

class ViewPagerAdapter(private val ctx: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var title: List<String> = listOf(ctx.resources.getString(R.string.menu_movie), ctx.resources.getString(R.string.menu_tv_show))

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return MovieFragment()
            }
            1 -> {
                return TVShowFragment()
            }
            else -> return MovieFragment()
        }
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}  
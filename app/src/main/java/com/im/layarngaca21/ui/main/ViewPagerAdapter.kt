package com.im.layarngaca21.ui.main

import android.content.Context
import com.im.layarngaca21.R

class ViewPagerAdapter(private val ctx: Context, fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private var title: List<String> = listOf(ctx.resources.getString(R.string.menu_movie), ctx.resources.getString(R.string.menu_tv_show))

    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
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
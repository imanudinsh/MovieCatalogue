package com.im.topmovie.view.main

import android.content.Context
import androidx.fragment.app.Fragment
import com.im.topmovie.R

class ViewPagerAdapter(private val ctx: Context, fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private var title: List<String> = listOf(ctx.resources.getString(R.string.menu_movie), ctx.resources.getString(R.string.menu_tv_show))

    override fun getItem(position: Int): Fragment {
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
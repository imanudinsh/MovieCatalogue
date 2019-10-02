package com.im.layarngaca21.view.favorite

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.im.layarngaca21.R

class FavoriteViewPagerAdapter(private val ctx: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var title: List<String> = listOf(ctx.resources.getString(R.string.menu_movie), ctx.resources.getString(R.string.menu_tv_show))

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return FavoriteMovieFragment()
            }
            1 -> {
                return FavoriteTVShowFragment()
            }
            else -> return FavoriteMovieFragment()
        }
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}  
package com.im.layarngaca21.view.favorite

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.im.layarngaca21.R

class FavoriteViewPagerAdapter(private val ctx: Context, fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private var title: List<String> = listOf(ctx.resources.getString(R.string.menu_movie), ctx.resources.getString(R.string.menu_tv_show))

    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
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
package com.im.layarngaca21.ui.main

import android.app.Application
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class MoviesViewModelTest {

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        viewModel = MoviesViewModel(Application())
    }

    @Test
    fun getMovies(){
        viewModel.setMovies()
        Thread.sleep(5000)
        val listMovies = viewModel.getMovies()
        assertNotNull(listMovies)
        assertEquals(20, listMovies.value?.size)
    }


}
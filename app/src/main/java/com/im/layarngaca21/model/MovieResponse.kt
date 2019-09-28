package com.im.layarngaca21.model

import com.im.layarngaca21.model.Movie

data class MovieResponse (
    var page: Int? = 0,
    var results: List<Movie>? = null
)
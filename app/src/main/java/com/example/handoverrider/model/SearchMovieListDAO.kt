package com.example.handoverrider.model

 import com.example.handoverrider.database.MovieListEntity

data class SearchMovieListDAO(
    val Response: String,
    val Search: List<MovieListEntity>,
    val totalResults: String
)
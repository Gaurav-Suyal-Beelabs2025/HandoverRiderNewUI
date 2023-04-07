package com.example.handoverrider.api

import com.example.handoverrider.model.SearchMovieListDAO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RiderServiceAPI {

    @GET("?")
    suspend fun getMovieList(
        @Query("apikey") apikey: String,
        @Query("s") queryString: String
    ): Response<SearchMovieListDAO>

}
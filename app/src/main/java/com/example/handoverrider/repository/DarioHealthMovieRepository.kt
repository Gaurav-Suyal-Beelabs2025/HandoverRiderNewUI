package com.example.handoverrider.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.handoverrider.BuildConfig
import com.example.handoverrider.R
import com.example.handoverrider.api.RiderServiceAPI
import com.example.handoverrider.database.FavoritesMovieListDatabase
import com.example.handoverrider.database.MovieListEntity
import com.example.handoverrider.networkUtils.NetworkUtils
import com.google.gson.Gson
import java.util.*

class DarioHealthMovieRepository(
    private val darioHealthServiceAPI: RiderServiceAPI,
    private val favoritesMovieListDatabase: FavoritesMovieListDatabase,
    private val applicationContext: Context
) {
    var TAG = DarioHealthMovieRepository::class.java.simpleName;

    private val movieListLiveData = MutableLiveData<Response<List<MovieListEntity>>>()
    private val favoritesMovieListLiveData = MutableLiveData<Response<List<MovieListEntity>>>()

    val movieLiveData: LiveData<Response<List<MovieListEntity>>>
        get() = movieListLiveData


    val favoritesMovieLiveData: LiveData<Response<List<MovieListEntity>>>
        get() = favoritesMovieListLiveData


    suspend fun setFavoritesMovieStatus(position: Int) {
        movieListLiveData.getValue()?.data?.get(position)?.isFavoriteMovie =
            movieListLiveData.getValue()?.data?.get(position)?.isFavoriteMovie != true;
        movieListLiveData.getValue()?.data?.get(position)
            ?.let { favoritesMovieListDatabase.movieListDao().addFavoriteMovie(it) }
        Collections.sort(movieListLiveData.getValue()?.data, compareByTitle);
        movieListLiveData.postValue(Response.Success(movieListLiveData.getValue()?.data))
        if (BuildConfig.IS_LOG_ENABLED)
            Log.e(TAG, "setFavoritesMovieStatus")

    }

    suspend fun getMovieList(searchMovieString: String) {
        movieListLiveData.postValue(Response.Loading(true))
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            try {
                val result =
                    darioHealthServiceAPI.getMovieList(BuildConfig.APIKey, searchMovieString)
                if (BuildConfig.IS_LOG_ENABLED)
                    Log.e(TAG, Gson().toJson(result.body()))
                if (result?.body() != null && result?.isSuccessful) {
                    if (result.body()!!.Search != null && result.body()!!.Search.isNotEmpty()) {
                        Collections.sort(result.body()!!.Search, compareByTitle);
                        movieListLiveData.postValue(Response.Success(result.body()!!.Search))

                    } else {
                        movieListLiveData.postValue(
                            Response.Error(
                                applicationContext.resources.getString(
                                    R.string.data_not_available
                                )
                            )
                        )
                    }

                } else {
                    movieListLiveData.postValue(
                        Response.Error(
                            applicationContext.resources.getString(
                                R.string.contact_to_support_team
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                movieListLiveData.postValue(Response.Error(e.message.toString()))
            }

        } else {
            movieListLiveData.postValue(Response.Error(applicationContext.resources.getString(R.string.no_internet_connection)))
        }
        if (BuildConfig.IS_LOG_ENABLED)
            Log.e(TAG, "getMovieList")

    }
//
//    suspend fun getAllFavoritesMovieList() {
//        favoritesMovieListLiveData.postValue(Response.Loading(true))
//        try {
//            val result: List<MovieListEntity> =
//                favoritesMovieListDatabase.movieListDao().getAllFavoriteMoviesList()
//            if (BuildConfig.IS_LOG_ENABLED)
//                Log.e("result", "" + result.size)
//            if (result != null && result.isNotEmpty()) {
//                Collections.sort(result, compareByTitle);
//                favoritesMovieListLiveData.postValue(Response.Success(result))
//            } else {
//                favoritesMovieListLiveData.postValue(
//                    Response.Error(
//                        applicationContext.resources.getString(
//                            R.string.data_not_available
//                        )
//                    )
//                )
//            }
//
//
//        } catch (e: Exception) {
//            favoritesMovieListLiveData.postValue(Response.Error(e.message.toString()))
//        }
//        if (BuildConfig.IS_LOG_ENABLED)
//            Log.e(TAG, "getAllFavoritesMovieList")
//    }
//
//    suspend fun setFavoritesMovieStatusDB(position: Int) {
//        favoritesMovieListLiveData.getValue()?.data?.get(position)?.isFavoriteMovie =
//            favoritesMovieListLiveData.getValue()?.data?.get(position)?.isFavoriteMovie != true;
//        favoritesMovieListLiveData.getValue()?.data?.get(position)
//            ?.let { favoritesMovieListDatabase.movieListDao().addFavoriteMovie(it) }
//        Collections.sort(favoritesMovieListLiveData.getValue()?.data, compareByTitle);
//        favoritesMovieListLiveData.postValue(Response.Success(favoritesMovieListLiveData.getValue()?.data))
//        if (BuildConfig.IS_LOG_ENABLED)
//            Log.e(TAG, "setFavoritesMovieStatusDB")
//
//    }
//
//    suspend fun deleteFavoritesMovie(imdbID: String) {
//        //   quotesLiveData.postValue(Response.Loading(true))
//        try {
//            val result =
//                favoritesMovieListDatabase.movieListDao().deleteFavoriteMovieByimdbID(imdbID)
//            if (result != null && result > 0) {
//                // val general = GeneralMessageData(100, "Deleted successfully")
//                movieListLiveData.postValue(Response.OnlySuccessMessage("Deleted successfully"))
//            } else {
//                movieListLiveData.postValue(
//                    Response.Error(
//                        applicationContext.resources.getString(
//                            R.string.data_not_available
//                        )
//                    )
//                )
//            }
//
//
//        } catch (e: Exception) {
//            movieListLiveData.postValue(Response.Error(e.message.toString()))
//        }
//        if (BuildConfig.IS_LOG_ENABLED)
//            Log.e(TAG, "deleteFavoritesMovie")
//
//    }

    var compareByTitle: Comparator<MovieListEntity> =
        Comparator<MovieListEntity> { o1: MovieListEntity, o2: MovieListEntity ->
            o1.Title.compareTo(o2.Title)
        }


}








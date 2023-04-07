package com.example.handoverrider.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handoverrider.database.MovieListEntity
import com.example.handoverrider.repository.DarioHealthMovieRepository
import com.example.handoverrider.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: DarioHealthMovieRepository) : ViewModel() {

    fun getMovieList(searchMovieString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieList(searchMovieString)
        }
    }


    fun setFavoritesMovieStatus(position: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFavoritesMovieStatus(position!!)
        }
    }


    val liveDataMovieList: LiveData<Response<List<MovieListEntity>>>
        get() = repository.movieLiveData


}
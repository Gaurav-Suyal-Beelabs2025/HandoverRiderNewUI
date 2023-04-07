package com.example.handoverrider.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.handoverrider.repository.DarioHealthMovieRepository

class RiderViewModelFactory(private val repository: DarioHealthMovieRepository) :
     ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(repository) as T
    }

}
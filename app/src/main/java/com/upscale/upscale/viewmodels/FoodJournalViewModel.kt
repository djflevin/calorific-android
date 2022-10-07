package com.upscale.upscale.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.upscale.upscale.data.Repository
import kotlinx.coroutines.launch

class FoodJournalViewModel(private val repository: Repository) : ViewModel() {
    private val TAG = "FoodJournalViewModel"
    init {
        viewModelScope.launch {
            repository.checkMealsAndPopulate()
        }
    }

    val meals = repository.meals


}

class FoodJournalViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodJournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodJournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
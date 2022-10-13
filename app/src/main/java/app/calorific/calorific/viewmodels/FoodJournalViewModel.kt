package app.calorific.calorific.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.calorific.calorific.data.Repository
import app.calorific.calorific.data.food.Food
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FoodJournalViewModel(private val repository: Repository) : ViewModel() {
    private val TAG = "FoodJournalViewModel"

    val foods = MediatorLiveData<List<Food>>()
    val mealNames = repository.meals

    init {
        viewModelScope.launch {
            foods.addSource(repository.getFoods(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE))){
                foods.value = it
            }
        }
    }



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
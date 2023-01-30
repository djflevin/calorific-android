package app.calorific.calorific.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.calorific.calorific.data.Meal
import app.calorific.calorific.data.Repository
import app.calorific.calorific.data.food.Food
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FoodJournalViewModel(private val repository: Repository, val date: String = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) : ViewModel() {
    private val TAG = "FoodJournalViewModel"

    val meals =  MediatorLiveData<List<Meal>>()
    val mealNames = repository.meals

    init {
        viewModelScope.launch {
            meals.addSource(repository.getFoods(date)){foods ->
                meals.value = mealNames.map { mealName ->
                    Meal(
                        name = mealName,
                        foods = foods.filter { it.instance.meal == mealName }
                    )
                }
            }
        }
    }



}

class FoodJournalDayViewModelFactory(private val repository: Repository, private val date: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodJournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodJournalViewModel(repository, date) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
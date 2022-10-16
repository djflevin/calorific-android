package app.calorific.calorific.viewmodels

import android.util.Log
import androidx.lifecycle.*
import app.calorific.calorific.data.Repository
import app.calorific.calorific.data.food.FoodInfo
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FindFoodViewModel(private val repository: Repository) : ViewModel() {
    private val TAG = "FindFoodViewModel"

    val foodHistory = repository.foodHistory
    val meals = repository.meals

    var selectedMeal = meals[0]

    var selectedDate: String = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    private val _food = MutableLiveData<FoodInfo>()
    val food = _food as LiveData<FoodInfo>


    fun searchBarcode(code: String){
        viewModelScope.launch {
            try{
                val food = repository.searchCode(code)

                _food.value = food
            } catch (e: Exception){
                Log.e(TAG, "$e")
            }
        }
    }

    fun setMeal(meal: String){
        selectedMeal = meal
    }

    fun setDate(date: String){
        selectedDate = date
    }

    fun setFood(food: FoodInfo){
        _food.value = food
    }

    fun saveFood(food: FoodInfo, serving: Double){
        viewModelScope.launch{
            repository.saveFood(food, selectedMeal, selectedDate, serving)
        }
    }



}

class FindFoodViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindFoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FindFoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
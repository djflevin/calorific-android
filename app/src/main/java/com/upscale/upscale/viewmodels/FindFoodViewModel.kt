package com.upscale.upscale.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.upscale.upscale.data.FoodInfo
import com.upscale.upscale.data.Repository
import kotlinx.coroutines.launch

class FindFoodViewModel(private val repository: Repository) : ViewModel() {
    private val TAG = "FindFoodViewModel"

    val foodHistory = repository.foodHistory
    val meals = repository.meals

    var mealId = -1

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

    fun setMeal(id: Int){
        mealId = id
    }

    fun setFood(food: FoodInfo){
        _food.value = food
    }

    fun saveFood(food: FoodInfo, serving: Double){
        viewModelScope.launch{
            repository.saveFood(food, mealId, serving)
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
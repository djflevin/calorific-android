package com.upscale.upscale.data

import com.upscale.upscale.data.food.FoodInstance
import com.upscale.upscale.data.food.FoodDao
import com.upscale.upscale.data.meal.MealDao
import com.upscale.upscale.data.meal.MealInfo
import com.upscale.upscale.network.OpenFoodFactsApi
import java.util.*

class Repository(
    private val foodDao: FoodDao,
    private val mealDao: MealDao
    ) {
    private val TAG = "Repository"
    private val openFoodFactsApi = OpenFoodFactsApi

    val meals = mealDao.loadMeals()

    val foodHistory = foodDao.getRecentFoods()

    suspend fun searchCode(code: String) : FoodInfo{
        val localFoodInfo = foodDao.localSearchBarcode(code)
        return if(localFoodInfo!=null){
            foodDao.updateFood(localFoodInfo.copy(lastAccessed = Calendar.getInstance().time.time))
            localFoodInfo
        } else{
            val result = openFoodFactsApi.retrofitService.getFoodByBarcode(code)
            val newId = foodDao.insertNewFood(result.toFoodInfo())

            foodDao.getFood(newId.toInt())
        }
    }


    suspend fun saveFood(foodInfo: FoodInfo, mealId: Int, serving: Double){
        foodDao.saveFood(
            FoodInstance(
                mealId = mealId,
                foodId = foodInfo.id,
                offId = foodInfo.offId,
                serving = serving
            )
        )
    }

    suspend fun checkMealsAndPopulate(){
        if(!mealDao.checkIfMealsExists()){
            mealDao.insertMeals(
                listOf(
                    MealInfo(name = "Breakfast"),
                    MealInfo(name = "Lunch"),
                    MealInfo(name = "Dinner"),
                    MealInfo(name = "Snacks")
                )
            )
        }
    }


}
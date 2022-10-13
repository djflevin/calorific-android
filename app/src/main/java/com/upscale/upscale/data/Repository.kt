package com.upscale.upscale.data

import androidx.lifecycle.LiveData
import com.upscale.upscale.data.food.Food
import com.upscale.upscale.data.food.FoodDao
import com.upscale.upscale.data.food.FoodInstance
import com.upscale.upscale.network.OpenFoodFactsApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Repository(
    private val foodDao: FoodDao,
    ) {
    private val TAG = "Repository"
    private val openFoodFactsApi = OpenFoodFactsApi


    val meals = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

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

    fun getFoods(date: String) : LiveData<List<Food>> = foodDao.getFoodsByDate(date)




    suspend fun saveFood(foodInfo: FoodInfo, meal: String, serving: Double){
        foodDao.saveFood(
            FoodInstance(
                meal = meal,
                date = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE),
                foodId = foodInfo.id,
                offId = foodInfo.offId,
                serving = serving
            )
        )
    }


}
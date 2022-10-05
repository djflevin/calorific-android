package com.upscale.upscale.data.meal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface MealDao {
    @Transaction
    @Query("SELECT * FROM mealinfo")
    suspend fun getMeals() : List<Meal>

    @Transaction
    @Query("SELECT * FROM mealinfo")
    fun loadMeals() : LiveData<List<Meal>>

    @Query("SELECT EXISTS(SELECT * FROM mealinfo)")
    suspend fun checkIfMealsExists() : Boolean

    @Insert()
    suspend fun insertMeals(mealList: List<MealInfo>)
}
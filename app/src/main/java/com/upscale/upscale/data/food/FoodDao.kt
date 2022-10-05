package com.upscale.upscale.data.food

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.upscale.upscale.data.FoodInfo

@Dao
interface FoodDao {

    @Query("SELECT * FROM foodinfo WHERE id=:id")
    suspend fun getFood(id: Int) : FoodInfo

    @Update
    suspend fun updateFood(foodInfo: FoodInfo)

    @Query("SELECT * FROM foodinfo WHERE code = :code")
    suspend fun localSearchBarcode(code: String) : FoodInfo?

    @Query("SELECT * FROM foodinfo ORDER BY last_accessed DESC LIMIT 10")
    fun getRecentFoods() : LiveData<List<FoodInfo>>

    @Insert()
    suspend fun insertNewFood(food: FoodInfo) : Long

    @Insert()
    suspend fun saveFood(foodInstance: FoodInstance)
}
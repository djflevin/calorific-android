package app.calorific.calorific.data.food

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodDao {

    @Query("SELECT * FROM foodinfo WHERE id=:id")
    suspend fun getFood(id: Int) : FoodInfo

    @Transaction
    @Query("SELECT * FROM foodinstance WHERE date=:date")
    fun getFoodsByDate(date: String) : LiveData<List<Food>>

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
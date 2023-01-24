package app.calorific.calorific.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import app.calorific.calorific.data.food.Food
import app.calorific.calorific.data.food.FoodDao
import app.calorific.calorific.data.food.FoodInfo
import app.calorific.calorific.data.food.FoodInstance
import app.calorific.calorific.network.OpenFoodFactsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class Repository(
    private val foodDao: FoodDao,
    private val dataStore: DataStore<Preferences>
    ) {

    private val TAG = "Repository"
    private val openFoodFactsApi = OpenFoodFactsApi

    private val USER_NAME = stringPreferencesKey("user_name")


    val meals = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

    val foodHistory = foodDao.getRecentFoods()
    val nameFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Name Not Found"
    }

    suspend fun searchCode(code: String) : FoodInfo {
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

    suspend fun saveFood(foodInfo: FoodInfo, meal: String, date: String, serving: Double){
        foodDao.saveFood(
            FoodInstance(
                meal = meal,
                date = date,
                foodId = foodInfo.id,
                offId = foodInfo.offId,
                serving = serving
            )
        )
        foodDao.updateFood(foodInfo.copy(lastAccessed = Calendar.getInstance().time.time))
    }


}
package app.calorific.calorific.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import app.calorific.calorific.data.food.Food
import app.calorific.calorific.data.food.FoodDao
import app.calorific.calorific.data.food.FoodInfo
import app.calorific.calorific.data.food.FoodInstance
import app.calorific.calorific.data.user.UserPrefs
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

    private object PreferenceKeys{
        val USER_NAME = stringPreferencesKey("user_name")
        val CALORIES_GOAL = intPreferencesKey("calories_goal")
        val MACROS_PROTEIN_GOAL = intPreferencesKey("protein_goal")
        val MACROS_FAT_GOAL = intPreferencesKey("fat_goal")
        val MACROS_CARBS_GOAL = intPreferencesKey("carbs_goal")
    }


    val meals = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

    val foodHistory = foodDao.getRecentFoods()
    val preferencesFlow: Flow<UserPrefs> = dataStore.data.map { preferences ->
        val proteinGoal = preferences[PreferenceKeys.MACROS_PROTEIN_GOAL]
        val fatGoal = preferences[PreferenceKeys.MACROS_FAT_GOAL]
        val carbsGoal = preferences[PreferenceKeys.MACROS_CARBS_GOAL]
        val macrosGoals = if(proteinGoal!=null && fatGoal!=null && carbsGoal!=null){
            UserPrefs.MacrosGoals(
                proteinPercentageGoal = proteinGoal,
                fatPercentageGoal = fatGoal,
                carbPercentageGoal = carbsGoal
            )
        } else null
        UserPrefs(
            name = preferences[PreferenceKeys.USER_NAME] ?: "Name Not Found",
            calorieGoal = preferences[PreferenceKeys.CALORIES_GOAL],
            macrosGoals = macrosGoals
        )
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

    suspend fun saveName(){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_NAME] = "DANIEL"
        }
    }

    suspend fun saveCalorieGoal(newGoal: Int?) {
        dataStore.edit { preferences ->
            when(newGoal){
                null -> preferences.remove(PreferenceKeys.CALORIES_GOAL)
                else -> preferences[PreferenceKeys.CALORIES_GOAL] = newGoal
            }
        }
    }

    suspend fun saveMacrosGoals(newGoals: UserPrefs.MacrosGoals?) {
        dataStore.edit { preferences ->
            when(newGoals){
                null -> {
                    preferences.remove(PreferenceKeys.MACROS_PROTEIN_GOAL)
                    preferences.remove(PreferenceKeys.MACROS_FAT_GOAL)
                    preferences.remove(PreferenceKeys.MACROS_CARBS_GOAL)
                }
                else -> {
                    preferences[PreferenceKeys.MACROS_PROTEIN_GOAL] = newGoals.proteinPercentageGoal
                    preferences[PreferenceKeys.MACROS_FAT_GOAL] = newGoals.fatPercentageGoal
                    preferences[PreferenceKeys.MACROS_CARBS_GOAL] = newGoals.carbPercentageGoal
                }
            }
        }
    }


}
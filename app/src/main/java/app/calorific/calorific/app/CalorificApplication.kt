package app.calorific.calorific.app

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import app.calorific.calorific.data.Repository
import app.calorific.calorific.database.AppDatabase

class CalorificApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val userPreferencesDataStore by preferencesDataStore(name = "Test")
    val repository by lazy { Repository(database.foodDao(), userPreferencesDataStore) }
}
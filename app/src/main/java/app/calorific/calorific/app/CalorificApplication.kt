package app.calorific.calorific.app

import android.app.Application
import app.calorific.calorific.data.Repository
import app.calorific.calorific.database.AppDatabase

class CalorificApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.foodDao()) }
}
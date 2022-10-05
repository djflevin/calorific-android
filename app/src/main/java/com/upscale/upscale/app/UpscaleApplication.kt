package com.upscale.upscale.app

import android.app.Application
import com.upscale.upscale.data.Repository
import com.upscale.upscale.database.AppDatabase

class UpscaleApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.foodDao(), database.mealDao()) }
}
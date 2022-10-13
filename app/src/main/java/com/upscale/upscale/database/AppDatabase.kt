package com.upscale.upscale.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.upscale.upscale.data.FoodInfo
import com.upscale.upscale.data.food.FoodDao
import com.upscale.upscale.data.food.FoodInstance

@Database(entities = [FoodInfo::class, FoodInstance::class], version = 1, exportSchema = false) // enable exportSchema when finished
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "food")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
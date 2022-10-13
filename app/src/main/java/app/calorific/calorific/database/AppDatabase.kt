package app.calorific.calorific.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.calorific.calorific.data.food.FoodDao
import app.calorific.calorific.data.food.FoodInfo
import app.calorific.calorific.data.food.FoodInstance

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
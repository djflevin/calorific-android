package com.upscale.upscale.data.food

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.upscale.upscale.data.FoodInfo

@Entity
data class FoodInstance(
    @PrimaryKey(autoGenerate = true)val id: Int? = null,
    @ColumnInfo(name="meal_id")val mealId: Int,
    @ColumnInfo(name="food_info_id")val foodId: Int,
    @ColumnInfo(name="off_id")val offId: String,
    @ColumnInfo(name="serving")val serving: Double
    )

data class Food(
    @Embedded val instance: FoodInstance,
    @Relation(
        parentColumn = "food_info_id",
        entityColumn = "id"
    ) val info: FoodInfo
    ){

    // Getters for the specific nutritional values given the food type and serving
    val energy get() = info.metricToServingRatio * info.energy1 * instance.serving
    val protein get() = info.metricToServingRatio * info.protein1 * instance.serving
    val fat get() = info.metricServingValue * info.fats1 * instance.serving
    val carbs get() = info.metricServingValue * info.carbs1 * instance.serving
}
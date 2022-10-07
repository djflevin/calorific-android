package com.upscale.upscale.data.meal

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.upscale.upscale.data.food.Food
import com.upscale.upscale.data.food.FoodInstance

@Entity
data class MealInfo(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "name") val name: String
)

data class Meal(
    @Embedded val mealInfo: MealInfo,
    @Relation(
        parentColumn = "id",
        entityColumn="meal_id",
        entity = FoodInstance::class
    ) val foods: List<Food>
) {
    val energy get() = foods.sumOf { it.energy }
    val protein get() = foods.sumOf { it.protein }
}

package com.upscale.upscale.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodInfo(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name="off_id") val offId: String,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "product_name") val name: String?,
    @ColumnInfo(name = "energy") val energy: Double,
    @ColumnInfo(name = "energy_unit") val energyUnit: String,
    @ColumnInfo(name = "fats") val fats: Double,
    @ColumnInfo(name = "carbohydrates") val carbs: Double,
    @ColumnInfo(name = "protein") val protein: Double,
    @ColumnInfo(name = "serving_value") val servingValue: Double, // e.g. biscuits
    @ColumnInfo(name = "serving_unit") val servingUnit: String,
    @ColumnInfo(name = "metric_serving_value") val metricServingValue: Double, // e.g. 2 biscuits = 16.67 g
    @ColumnInfo(name = "metric_serving_unit") val metricServingUnit: String,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Long? = null
)


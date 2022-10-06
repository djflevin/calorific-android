package com.upscale.upscale.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class FoodInfo(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name="off_id") val offId: String,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "product_name") val name: String?,
    @ColumnInfo(name = "energy_100") val energy100: Double,
    @ColumnInfo(name = "energy_unit") val energyUnit: String,
    @ColumnInfo(name = "fats_100") val fats100: Double,
    @ColumnInfo(name = "carbohydrates_100") val carbs100: Double,
    @ColumnInfo(name = "protein_100") val protein100: Double,
    @ColumnInfo(name = "serving_value") val servingValue: Double, // e.g. biscuits
    @ColumnInfo(name = "serving_unit") val servingUnit: String,
    @ColumnInfo(name = "metric_serving_value") val metricServingValue: Double, // e.g. 2 biscuits = 16.67 g
    @ColumnInfo(name = "metric_serving_unit") val metricServingUnit: String,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Long? = null
){

    // Convenience values
    @Ignore val metricToServingRatio = metricServingValue / servingValue
    @Ignore val energy1 = energy100 / 100
    @Ignore val protein1 = protein100 / 100
    @Ignore val fats1 = fats100 / 100
    @Ignore val carbs1 = carbs100 / 100
}
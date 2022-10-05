package com.upscale.upscale.data.food

import android.util.Log
import com.squareup.moshi.Json
import com.upscale.upscale.data.FoodInfo
import java.util.*

data class Serving(
    val metricValue: Double,
    val metricUnit: String,
    val servingValue: Double,
    val servingUnit: String
)

data class FoodReceiver(
    @field:Json(name = "code")val code: String?,
    @field:Json(name = "product")val product: Map<String,Any>,
) {



    fun toFoodInfo() : FoodInfo {
        val nutrients = product.get("nutriments") as Map<*, *>
        val servingDetails = unpackServingSize(product.get("serving_size") as? String?)

        return FoodInfo(
            code = code,
            offId = product.get("_id") as String,
            name = product.get("product_name") as String,
            energy = nutrients["energy_100g"] as Double,
            energyUnit = nutrients["energy_unit"] as String,
            protein = nutrients["proteins_100g"] as Double,
            fats = nutrients["fat_100g"] as Double,
            carbs = nutrients["carbohydrates_100g"] as Double,
            servingValue = servingDetails.servingValue,
            servingUnit = servingDetails.servingUnit,
            metricServingValue = servingDetails.metricValue,
            metricServingUnit = servingDetails.metricUnit,
            lastAccessed = Calendar.getInstance().time.time
        )
    }

    private fun unpackServingSize(servingSize: String?): Serving{
        val regex1 = "([0-9]+(\\.[0-9]+)?) ([a-zA-Z]+) \\(([0-9]+) ([a-zA-Z]+)\\)".toRegex()
        val regex2 = """([0-9]+(\\.[0-9]+)?)\s?([a-zA-Z]+)""".toRegex()
        return when{
            servingSize!=null && regex1.matches(servingSize) -> {
                val groups = regex1.find(servingSize)!!.groups
                Serving(
                    metricValue = groups[1]!!.value.toDouble(),
                    metricUnit = groups[3]!!.value,
                    servingValue = groups[4]!!.value.toDouble(),
                    servingUnit = groups[5]!!.value
                )
            }
            servingSize!=null && regex2.matches(servingSize) -> {
                val groups = regex2.find(servingSize)!!.groups
                Serving(
                    metricValue = groups[1]!!.value.toDouble(),
                    metricUnit = groups[3]!!.value,
                    servingValue = groups[1]!!.value.toDouble(),
                    servingUnit = groups[3]!!.value
                )
            }
            else -> {
                val dataPreparedPer = product.get("nutrition_data_prepared_per") as String
                if(dataPreparedPer == "100ml"){
                    Serving(
                        metricValue = 100.0,
                        metricUnit = "ml",
                        servingValue = 100.0,
                        servingUnit = "ml"
                    )
                } else{
                    Serving(
                        metricValue = 100.0,
                        metricUnit = "g",
                        servingValue = 100.0,
                        servingUnit = "g"
                    )
                }
            }
        }
    }

}


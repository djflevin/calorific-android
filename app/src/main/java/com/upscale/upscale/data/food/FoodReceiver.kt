package com.upscale.upscale.data.food

import com.squareup.moshi.Json
import com.upscale.upscale.data.FoodInfo
import com.upscale.upscale.utils.KCAL_TO_KJ
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
        val nutrients = product["nutriments"] as Map<*, *>
        val servingDetails = unpackServingSize(product["serving_size"] as? String?)

        val energy = nutrients["energy_100g"] as Double
        val energyUnit = nutrients["energy_unit"] as String

        val energyKj100 = nutrients["energy-kj_100g"] as? Double? ?: if(energyUnit == "kJ") energy else null
        val energyCals100 = nutrients["energy-kcal_100g"] as? Double? ?: if(energyUnit == "kcal") energy else null

        return FoodInfo(
            code = code,
            offId = product.get("_id") as String,
            name = product.get("product_name") as String,
            energyCals100 = energyCals100 ?: (energyKj100!! / KCAL_TO_KJ),
            energyKj100 =energyKj100 ?: (energyCals100!! * KCAL_TO_KJ),
            protein100 = nutrients["proteins_100g"] as Double,
            fats100 = nutrients["fat_100g"] as Double,
            carbs100 = nutrients["carbohydrates_100g"] as Double,
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


package app.calorific.calorific.data

import app.calorific.calorific.data.food.Food

// Convenience class for use in RecyclerView Adapters
data class Meal(
    val name: String,
    val foods: List<Food>
) {
    val energy get() = foods.sumOf { it.energy }
}

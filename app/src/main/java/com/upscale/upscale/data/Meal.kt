package com.upscale.upscale.data

import com.upscale.upscale.data.food.Food

// Convenience class for use in RecyclerView Adapters
data class Meal(
    val name: String,
    val foods: List<Food>
)

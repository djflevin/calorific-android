package com.upscale.upscale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upscale.upscale.data.food.Food
import com.upscale.upscale.databinding.ItemJournalFoodBinding

class FoodJournalChildAdapter : ListAdapter<Food,FoodJournalChildAdapter.ViewHolder>(FoodDiffUtil){

    object FoodDiffUtil: DiffUtil.ItemCallback<Food>(){
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.instance.id == newItem.instance.id
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(binding: ItemJournalFoodBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var food: Food

        val foodNameTextView = binding.foodNameTextView
        val energyTextView = binding.energyTextView

        fun bind(){
            food = getItem(bindingAdapterPosition)

            foodNameTextView.text = food.info.name
            energyTextView.text = String.format(
                "%.0f",
                food.energy
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemJournalFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

}
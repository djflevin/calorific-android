package com.upscale.upscale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.upscale.upscale.data.meal.Meal
import com.upscale.upscale.databinding.ItemJournalMealBinding
import com.upscale.upscale.utils.dp

class FoodJournalAdapter() : ListAdapter<Meal,FoodJournalAdapter.ViewHolder>(MealDiffUtil) {
    var onAddFoodButtonPressed: ((Meal) -> Unit)? = null


    object MealDiffUtil: DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(binding: ItemJournalMealBinding): RecyclerView.ViewHolder(binding.root){
        lateinit var meal: Meal

        val adapter = FoodJournalChildAdapter()

        val recyclerView = binding.recyclerView
        val textView = binding.textView

        fun bind(){
            meal = getItem(bindingAdapterPosition)
            textView.text = meal.mealInfo.name
            adapter.submitList(meal.foods)
        }

        init{
            recyclerView.adapter = adapter
            recyclerView.itemAnimator = null

            binding.button.setOnClickListener{
                onAddFoodButtonPressed?.invoke(meal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(ItemJournalMealBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.recyclerView.layoutManager = LinearLayoutManager(parent.context)
        val divider = MaterialDividerItemDecoration(parent.context, LinearLayoutManager.VERTICAL)
        divider.dividerInsetStart = 16.dp
        holder.recyclerView.addItemDecoration(divider)

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}
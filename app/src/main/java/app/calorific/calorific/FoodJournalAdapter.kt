package app.calorific.calorific

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.calorific.calorific.data.Meal
import app.calorific.calorific.databinding.ItemJournalMealBinding
import app.calorific.calorific.utils.dp
import com.google.android.material.divider.MaterialDividerItemDecoration

class FoodJournalAdapter : ListAdapter<Meal,FoodJournalAdapter.ViewHolder>(MealDiffUtil) {
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

        private val adapter = FoodJournalChildAdapter()

        val recyclerView = binding.recyclerView
        private val textView = binding.textView

        fun bind(){
            meal = getItem(bindingAdapterPosition)
            textView.text = meal.name
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
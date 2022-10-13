package app.calorific.calorific

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.calorific.calorific.data.food.FoodInfo
import app.calorific.calorific.databinding.ItemSearchRecentFoodBinding

class SearchFoodAdapter : ListAdapter<FoodInfo,SearchFoodAdapter.ViewHolder>(FoodDiffUtil) {
    var onFoodPressed: ((FoodInfo) -> Unit)? = null

    object FoodDiffUtil: DiffUtil.ItemCallback<FoodInfo>(){
        override fun areItemsTheSame(oldItem: FoodInfo, newItem: FoodInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FoodInfo, newItem: FoodInfo): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(binding: ItemSearchRecentFoodBinding): RecyclerView.ViewHolder(binding.root){
        lateinit var foodInfo: FoodInfo


        private val textView = binding.foodNameTextView

        fun bind(){
            foodInfo = getItem(bindingAdapterPosition)
            textView.text = foodInfo.name
        }

        init{
            binding.foodItemLayout.setOnClickListener{
                onFoodPressed?.invoke(foodInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchRecentFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}
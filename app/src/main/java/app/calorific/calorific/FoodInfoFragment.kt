package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.databinding.FragmentFoodInfoBinding
import app.calorific.calorific.viewmodels.FindFoodViewModel
import app.calorific.calorific.viewmodels.FindFoodViewModelFactory
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class FoodInfoFragment : Fragment() {

    private var _binding: FragmentFoodInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindFoodViewModel by navGraphViewModels(R.id.addFoodGraph) {
        FindFoodViewModelFactory((requireActivity().application as CalorificApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fun formatNutritionValueToString(servingValue: Double, metricToServingRatio: Double, nutritionValuePer1: Double): String{
            return "${String.format("%.1f",
                servingValue * metricToServingRatio * nutritionValuePer1
                )} g"
        }

        viewModel.food.observe(viewLifecycleOwner) { foodInfo ->
            binding.foodNameTextView.text = foodInfo.name
            binding.barcodeTextView.text = "Barcode: ${foodInfo.code}"

            binding.carbsTextView.text = formatNutritionValueToString(foodInfo.servingValue, foodInfo.metricToServingRatio, foodInfo.carbs1)
            binding.fatTextView.text = formatNutritionValueToString(foodInfo.servingValue, foodInfo.metricToServingRatio, foodInfo.fats1)
            binding.proteinTextView.text = formatNutritionValueToString(foodInfo.servingValue, foodInfo.metricToServingRatio, foodInfo.protein1)
            binding.caloriesTextView.text = String.format(
                "%.0f",
                (foodInfo.energyCals1 * foodInfo.servingValue * foodInfo.metricToServingRatio)
            )

            binding.caloriesLabel.text = "Cals"

            binding.servingTextInputLayout.suffixText = foodInfo.servingUnit
            binding.servingEditText.setText(foodInfo.servingValue.toString())
            binding.servingTextInputLayout.isHintAnimationEnabled = true

            binding.servingEditText.addTextChangedListener {
                it?.let {
                    val newValue = when (it.isEmpty()) {
                        true -> 0.0
                        false -> it.toString().toDouble()
                    }
                    binding.caloriesTextView.text = String.format(
                        "%.0f",
                        (newValue * foodInfo.metricToServingRatio * foodInfo.energyCals1)
                    )
                    binding.carbsTextView.text = formatNutritionValueToString(newValue, foodInfo.metricToServingRatio, foodInfo.carbs1)
                    binding.fatTextView.text = formatNutritionValueToString(newValue, foodInfo.metricToServingRatio, foodInfo.fats1)
                    binding.proteinTextView.text = formatNutritionValueToString(newValue, foodInfo.metricToServingRatio, foodInfo.protein1)

                }
            }

            binding.addFoodFloatingActionButton.setOnClickListener {
                viewModel.saveFood(foodInfo, binding.servingEditText.text.toString().toDouble())
                findNavController().navigate(R.id.action_addFoodGraph_to_foodJournalHostFragment)
            }
        }

        (binding.mealTextView as MaterialAutoCompleteTextView).apply {
            setSimpleItems(viewModel.meals.toTypedArray())
            setText(viewModel.selectedMeal)
            addTextChangedListener{ editable ->
                viewModel.setMeal(editable.toString())
            }
        }

        binding.mealTextInputLayout.isHintAnimationEnabled = true // Enables hint label animations after text is set to stop jumping animations


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.upscale.upscale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.upscale.upscale.app.UpscaleApplication
import com.upscale.upscale.databinding.FragmentFoodInfoBinding
import com.upscale.upscale.viewmodels.FindFoodViewModel
import com.upscale.upscale.viewmodels.FindFoodViewModelFactory

class FoodInfoFragment : Fragment() {

    private var _binding: FragmentFoodInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindFoodViewModel by navGraphViewModels(R.id.addFoodGraph) {
        FindFoodViewModelFactory((requireActivity().application as UpscaleApplication).repository)
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


        viewModel.food.observe(viewLifecycleOwner) { foodInfo ->
            val servingToMetricRatio = foodInfo.metricServingValue / foodInfo.servingValue

            binding.foodNameTextView.text = foodInfo.name
            binding.barcodeTextView.text = "Barcode: ${foodInfo.code}"

            binding.carbsTextView.text = "${foodInfo.carbs} g"
            binding.fatTextView.text = "${foodInfo.fats} g"
            binding.proteinTextView.text = "${foodInfo.protein} g"
            binding.caloriesTextView.text = String.format(
                "%.0f",
                (foodInfo.energy * foodInfo.servingValue * servingToMetricRatio / 100)
            )
            binding.caloriesLabel.text = foodInfo.energyUnit

            binding.servingTextInputLayout.suffixText = foodInfo.servingUnit
            binding.servingEditText.setText(foodInfo.servingValue.toString())

            binding.servingEditText.addTextChangedListener {
                it?.let {
                    val newValue = when (it.isEmpty()) {
                        true -> 0.0
                        false -> it.toString().toDouble()
                    }
                    binding.caloriesTextView.text = String.format(
                        "%.0f",
                        (newValue * servingToMetricRatio * foodInfo.energy / 100)
                    )
                }
            }

            binding.addFoodFloatingActionButton.setOnClickListener {
                viewModel.saveFood(foodInfo, binding.servingEditText.text.toString().toDouble())
                findNavController().navigate(R.id.action_addFoodGraph_to_foodJournalFragment)
            }
        }
        viewModel.meals.observe(viewLifecycleOwner) { mealList ->
            (binding.mealTextView as MaterialAutoCompleteTextView).setSimpleItems(mealList.map { it.mealInfo.name }
                .toTypedArray())
            (binding.mealTextView as MaterialAutoCompleteTextView).apply {
                setText(
                    mealList.find { it.mealInfo.id == viewModel.mealId }?.mealInfo?.name ?: "",
                    false
                )
                addTextChangedListener { editable ->
                    mealList.find { it.mealInfo.name == editable.toString() }?.mealInfo?.id?.let { viewModel.setMeal(it) }
                }
            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
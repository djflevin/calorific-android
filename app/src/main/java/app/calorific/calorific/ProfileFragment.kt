package app.calorific.calorific

import android.app.AlertDialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.databinding.DialogChangeCalorieGoalBinding
import app.calorific.calorific.databinding.DialogChangeMacrosBinding
import app.calorific.calorific.databinding.FragmentProfileBinding
import app.calorific.calorific.viewmodels.UserViewModel
import app.calorific.calorific.viewmodels.UserViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory((requireActivity().application as CalorificApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.name.observe(viewLifecycleOwner) {
            binding.userNameTextView.text = it
        }
        viewModel.macrosGoals.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.carbsGoalValueTextView.text = "${it.carbPercentageGoal} %"
                binding.proteinGoalValueTextView.text = "${it.proteinPercentageGoal} %"
                binding.fatGoalValueTextView.text = "${it.fatPercentageGoal} %"
            } else {
                binding.carbsGoalValueTextView.text = ""
                binding.proteinGoalValueTextView.text = ""
                binding.fatGoalValueTextView.text = ""
            }
        }

        viewModel.caloriesGoal.observe(viewLifecycleOwner){
            it?.let { binding.caloriesGoalValueTextView.text = it.toString() }
        }

        binding.caloriesGoalLayout.setOnClickListener { showCaloriesPopup() }

        val macrosListener = OnClickListener { showMacrosPopup() }
        binding.carbsGoalLayout.setOnClickListener (macrosListener)
        binding.proteinGoalLayout.setOnClickListener(macrosListener)
        binding.fatGoalLayout.setOnClickListener(macrosListener)

    }

    private fun showCaloriesPopup(){
        val dialogBinding = DialogChangeCalorieGoalBinding.inflate(layoutInflater)
        dialogBinding.caloriesGoalEditText.setText((viewModel.caloriesGoal.value ?: "").toString())
        MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root)
            .setTitle("Set Daily Calorie Goal")
            .setPositiveButton("Save") {dialog, _ ->
                viewModel.updateCalorieGoal(dialogBinding.caloriesGoalEditText.text.toString().toInt())
            }
            .setNegativeButton("Cancel") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showMacrosPopup(){
        val dialogBinding = DialogChangeMacrosBinding.inflate(layoutInflater)

        val percentsArray = Array(101) { i -> "$i %" }
        for(picker in listOf(
            dialogBinding.carbohydratesPercentageNumberPicker,
            dialogBinding.fatPercentageNumberPicker,
            dialogBinding.proteinPercentageNumberPicker
        )){
            picker.minValue = 0
            picker.maxValue = 100
            picker.displayedValues = percentsArray
            picker.wrapSelectorWheel = false
        }

        viewModel.macrosGoals.observe(viewLifecycleOwner){
            it?.let {
                dialogBinding.carbohydratesPercentageNumberPicker.value = it.carbPercentageGoal
                dialogBinding.fatPercentageNumberPicker.value = it.fatPercentageGoal
                dialogBinding.proteinPercentageNumberPicker.value = it.proteinPercentageGoal

                val total = it.carbPercentageGoal + it.fatPercentageGoal + it.proteinPercentageGoal
                dialogBinding.totalPercentageTextView.text = "${total} %"
                if(total == 100){
                    val value = TypedValue()
                    requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, value, true)
                    dialogBinding.totalPercentageTextView.setTextColor(value.data)
                } else {
                    val value = TypedValue()
                    requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.colorError, value, true)
                    dialogBinding.totalPercentageTextView.setTextColor(value.data)
                }
            }
        }

        MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root)
            .setTitle("Set Macros")
            .setPositiveButton("Save") { dialog, _ ->
                viewModel.updateMacrosGoals(
                    dialogBinding.carbohydratesPercentageNumberPicker.value,
                    dialogBinding.fatPercentageNumberPicker.value,
                    dialogBinding.proteinPercentageNumberPicker.value
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
            .also {
                val button = it.getButton(AlertDialog.BUTTON_POSITIVE)



                NumberPicker.OnValueChangeListener { numberPicker, oldValue, newValue ->
                    val carbs = dialogBinding.carbohydratesPercentageNumberPicker.value
                    val fat = dialogBinding.fatPercentageNumberPicker.value
                    val protein = dialogBinding.proteinPercentageNumberPicker.value
                    val total = carbs + fat + protein
                    if(total == 100){
                        dialogBinding.totalPercentageTextView.text = "100 %"
                        val value = TypedValue()
                        requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, value, true)
                        dialogBinding.totalPercentageTextView.setTextColor(value.data)

                        button.isEnabled = true
                    } else {
                        dialogBinding.totalPercentageTextView.text = "${total} %"
                        val value = TypedValue()
                        requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.colorError, value, true)
                        dialogBinding.totalPercentageTextView.setTextColor(value.data)
                        button.isEnabled = false
                    }
        }.also {
                dialogBinding.carbohydratesPercentageNumberPicker.setOnValueChangedListener(it)
                dialogBinding.fatPercentageNumberPicker.setOnValueChangedListener(it)
                dialogBinding.proteinPercentageNumberPicker.setOnValueChangedListener(it)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
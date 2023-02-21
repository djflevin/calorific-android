package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.databinding.DialogChangeCalorieGoalBinding
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

        viewModel.caloriesGoal.observe(viewLifecycleOwner){
            it?.let { binding.caloriesGoalValueTextView.text = it.toString() }
        }

        binding.caloriesGoalLayout.setOnClickListener { showCaloriesPopup() }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
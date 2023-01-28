package app.calorific.calorific

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.databinding.FragmentProfileBinding
import app.calorific.calorific.viewmodels.UserViewModel
import app.calorific.calorific.viewmodels.UserViewModelFactory

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
            if(!binding.calorieGoalEditText.isFocused){
                it?.let { binding.calorieGoalEditText.setText(it.toString()) }
            }
        }


        binding.calorieGoalEditText.doAfterTextChanged {
            viewModel.updateCalorieGoal(it.toString().toIntOrNull())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
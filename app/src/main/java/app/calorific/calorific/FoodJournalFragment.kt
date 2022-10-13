package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.data.Meal
import app.calorific.calorific.databinding.FragmentFoodJournalBinding
import app.calorific.calorific.viewmodels.FoodJournalViewModel
import app.calorific.calorific.viewmodels.FoodJournalViewModelFactory

class FoodJournalFragment : Fragment() {
    private var _binding: FragmentFoodJournalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodJournalViewModel by activityViewModels {
        FoodJournalViewModelFactory((requireActivity().application as CalorificApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        val adapter = FoodJournalAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.itemAnimator = null


        // Configure adapter onClick behaviour
        adapter.onAddFoodButtonPressed = { meal ->
            setFragmentResult("meal", bundleOf("meal" to meal.name))
            findNavController().navigate(R.id.action_foodJournalFragment_to_addFoodGraph)
        }



        viewModel.foods.observe(viewLifecycleOwner) { foodList ->
            adapter.submitList(
                viewModel.mealNames.map { mealName ->
                    Meal(
                        name = mealName,
                        foods = foodList.filter { it.instance.meal == mealName }
                    )
                }
            )
            adapter.currentList.sumOf { it.energy }
        }


        /*binding.caloriesRemainingTextView.text = String.format(
            "%.0f",
            2700 - mealList.sumOf { it.energy }
        )*/


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
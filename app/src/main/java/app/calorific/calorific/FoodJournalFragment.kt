package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.data.Meal
import app.calorific.calorific.databinding.FragmentFoodJournalBinding
import app.calorific.calorific.viewmodels.FoodJournalViewModel
import app.calorific.calorific.viewmodels.FoodJournalViewModelFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FoodJournalFragment(val date: ZonedDateTime) : Fragment() {
    private var _binding: FragmentFoodJournalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodJournalViewModel by viewModels {
        FoodJournalViewModelFactory((requireActivity().application as CalorificApplication).repository, date.format(DateTimeFormatter.ISO_LOCAL_DATE))
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

        // Set top bar title based on date
        val presentDay = ZonedDateTime.now()
        val today = presentDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val yesterday = presentDay.minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)
        val tomorrow = presentDay.plusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)

        binding.topBarToolbar.title = when(date.format(DateTimeFormatter.ISO_LOCAL_DATE)){
            today -> "Today"
            yesterday -> "Yesterday"
            tomorrow -> "Tomorrow"
            else -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        }
        // Set up RecyclerView
        val adapter = FoodJournalAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.itemAnimator = null


        // Configure adapter onClick behaviour
        adapter.onAddFoodButtonPressed = { meal ->
            requireActivity().supportFragmentManager.setFragmentResult("add_food_info", bundleOf("meal" to meal.name, "date" to date.format(
                DateTimeFormatter.ISO_LOCAL_DATE)))
            findNavController().navigate(R.id.action_foodJournalHostFragment_to_addFoodGraph)
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
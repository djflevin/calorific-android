package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.data.Meal
import app.calorific.calorific.databinding.FragmentFoodJournalBinding
import app.calorific.calorific.viewmodels.FoodJournalDayViewModelFactory
import app.calorific.calorific.viewmodels.FoodJournalViewModel
import app.calorific.calorific.viewmodels.UserViewModel
import app.calorific.calorific.viewmodels.UserViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FoodJournalDayFragment : Fragment() {
    companion object{
        fun newInstance(date: LocalDate) = FoodJournalDayFragment().apply {
            this.date = date
        }
    }



    private var _binding: FragmentFoodJournalBinding? = null
    private val binding get() = _binding!!

    lateinit var date: LocalDate

    private val foodJournalViewModel: FoodJournalViewModel by viewModels {
        FoodJournalDayViewModelFactory(
            (requireActivity().application as CalorificApplication).repository,
            date.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory((requireActivity().application as CalorificApplication).repository)
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
        val presentDay = LocalDate.now()
        val today = presentDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val yesterday = presentDay.minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)
        val tomorrow = presentDay.plusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)

        binding.titleTextView.text = when(date.format(DateTimeFormatter.ISO_LOCAL_DATE)){
            today -> "Today"
            yesterday -> "Yesterday"
            tomorrow -> "Tomorrow"
            else -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        }

        // Configure calendar picker from top app bar
        binding.titleTextView.setOnClickListener {
            (parentFragment as FoodJournalHostFragment).showDatePickerPopup(date)
        }
        binding.backArrowImageView.setOnClickListener {
            (parentFragment as FoodJournalHostFragment).scrollPagerBack()
        }
        binding.forwardArrowImageView.setOnClickListener {
            (parentFragment as FoodJournalHostFragment).scrollPagerForward()
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



        var calorieGoal: Int? = null
        var meals: List<Meal> = listOf()

        fun refreshCalorieCounter(){
            when(calorieGoal){
                null -> binding.caloriesLayout.visibility = View.GONE
                else -> binding.caloriesLayout.visibility = View.VISIBLE
            }
            calorieGoal?.let { binding.remainingCaloriesValueTextView.text = String.format("%.0f", it - meals.sumOf { it.energy })}
        }

        userViewModel.caloriesGoal.observe(viewLifecycleOwner){
            calorieGoal = it
            refreshCalorieCounter()
        }

        foodJournalViewModel.meals.observe(viewLifecycleOwner){
            meals = it
            adapter.submitList(it)
            refreshCalorieCounter()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
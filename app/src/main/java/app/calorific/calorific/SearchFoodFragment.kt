package app.calorific.calorific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.calorific.calorific.app.CalorificApplication
import app.calorific.calorific.databinding.FragmentSearchFoodBinding
import app.calorific.calorific.utils.dp
import app.calorific.calorific.viewmodels.FindFoodViewModel
import app.calorific.calorific.viewmodels.FindFoodViewModelFactory
import com.google.android.material.divider.MaterialDividerItemDecoration

class SearchFoodFragment : Fragment() {
    private val TAG = "SearchFoodFragment"

    private var _binding: FragmentSearchFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindFoodViewModel by navGraphViewModels(R.id.addFoodGraph){
        FindFoodViewModelFactory((requireActivity().application as CalorificApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.setFragmentResultListener("add_food_info", viewLifecycleOwner) { _, bundle ->
            bundle.getString("meal")?.let { viewModel.setMeal(it) }
            bundle.getString("date")?.let { viewModel.setDate(it) }
        }

        binding.searchTextInputLayout.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_searchFoodFragment_to_scannerFragment)
        }

        val adapter = SearchFoodAdapter()
        adapter.onFoodPressed = { foodInfo ->
            viewModel.setFood(foodInfo)
            findNavController().navigate(R.id.action_searchFoodFragment_to_foodInfoFragment)
        }

        binding.recentFoodsRecyclerView.adapter = adapter
        binding.recentFoodsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.recentFoodsRecyclerView.addItemDecoration(
            MaterialDividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
                .apply {
                    dividerInsetStart = 8.dp
                    dividerInsetEnd = 8.dp
                }
        )

        viewModel.foodHistory.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
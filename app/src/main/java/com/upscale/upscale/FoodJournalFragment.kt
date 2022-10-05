package com.upscale.upscale

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
import com.upscale.upscale.app.UpscaleApplication
import com.upscale.upscale.databinding.FragmentFoodJournalBinding
import com.upscale.upscale.viewmodels.FoodJournalViewModel
import com.upscale.upscale.viewmodels.FoodJournalViewModelFactory

class FoodJournalFragment : Fragment() {
    private var _binding : FragmentFoodJournalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodJournalViewModel by activityViewModels {
        FoodJournalViewModelFactory((requireActivity().application as UpscaleApplication).repository)
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
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.itemAnimator = null



        // Configure adapter onClick behaviour
        adapter.onAddFoodButtonPressed = { meal ->
            setFragmentResult("meal", bundleOf("meal_id" to meal.mealInfo.id))
            findNavController().navigate(R.id.action_foodJournalFragment_to_addFoodGraph)
        }

        viewModel.meals.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package app.calorific.calorific

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import app.calorific.calorific.databinding.FragmentFoodJournalHostBinding
import java.time.ZonedDateTime

class FoodJournalHostFragment : Fragment() {
    private val TAG = "FoodJournalHostFragment"

    private var _binding: FragmentFoodJournalHostBinding? = null
    private val binding get() = _binding!!

    private val presentDate = ZonedDateTime.now()

    var viewPagerPosition = 5000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJournalHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DayPagerAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(viewPagerPosition, false)
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerPosition = position
            }
        }
        )

    }

    private inner class DayPagerAdapter: FragmentStateAdapter(this){
        override fun getItemCount(): Int = 10000

        override fun createFragment(position: Int): Fragment {
            Log.d(TAG, position.toString())
            return FoodJournalFragment(
                date = presentDate.plusDays((position-5000).toLong()
                )
            )
        }

    }

}
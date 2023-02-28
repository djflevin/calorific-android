package app.calorific.calorific.viewmodels

import androidx.lifecycle.*
import app.calorific.calorific.data.Repository
import app.calorific.calorific.data.user.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {
    val name = MediatorLiveData<String>()
    val caloriesGoal = MediatorLiveData<Int?>()
    val macrosGoals = MediatorLiveData<UserPrefs.MacrosGoals?>()

    init {
        viewModelScope.launch {
            val preferencesLiveData = repository.preferencesFlow.asLiveData(Dispatchers.Main)
            name.addSource(preferencesLiveData){
                name.value = it.name
            }
            caloriesGoal.addSource(preferencesLiveData){
                caloriesGoal.value = it.calorieGoal
            }
            macrosGoals.addSource(preferencesLiveData){
                macrosGoals.value = it.macrosGoals
            }
        }
    }

    fun updateCalorieGoal(newGoal: Int?){
        viewModelScope.launch {
            repository.saveCalorieGoal(newGoal)
        }
    }

    fun updateMacrosGoals(carbs: Int, fats: Int, protein: Int){
        viewModelScope.launch {
            repository.saveMacrosGoals(
                UserPrefs.MacrosGoals(
                    proteinPercentageGoal = protein,
                    fatPercentageGoal = fats,
                    carbPercentageGoal = carbs
                )
            )
        }
    }
}

class UserViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
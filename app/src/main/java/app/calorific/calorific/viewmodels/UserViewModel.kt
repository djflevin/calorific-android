package app.calorific.calorific.viewmodels

import androidx.lifecycle.*
import app.calorific.calorific.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {
    val name = MediatorLiveData<String>()
    val caloriesGoal = MediatorLiveData<Int?>()

    init {
        viewModelScope.launch {
            val preferencesLiveData = repository.preferencesFlow.asLiveData(Dispatchers.Main)
            name.addSource(preferencesLiveData){
                name.value = it.name
            }
            caloriesGoal.addSource(preferencesLiveData){
                caloriesGoal.value = it.calorieGoal
            }
        }
    }

    fun updateCalorieGoal(newGoal: Int?){
        viewModelScope.launch {
            repository.saveCalorieGoal(newGoal)
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
package app.calorific.calorific.data.user

data class UserPrefs(
    val name: String,
    val calorieGoal: Int?,
    val macrosGoals: MacrosGoals? = null
) {
    data class MacrosGoals(
        val proteinPercentageGoal: Int,
        val fatPercentageGoal: Int,
        val carbPercentageGoal: Int
    )
}


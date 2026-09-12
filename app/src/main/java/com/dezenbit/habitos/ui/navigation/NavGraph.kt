package com.dezenbit.habitos.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.SettingsRepository
import com.dezenbit.habitos.ui.ViewModelFactory
import com.dezenbit.habitos.ui.screens.addedit.AddEditHabitScreen
import com.dezenbit.habitos.ui.screens.addedit.AddEditHabitViewModel
import com.dezenbit.habitos.ui.screens.detail.HabitDetailScreen
import com.dezenbit.habitos.ui.screens.detail.HabitDetailViewModel
import com.dezenbit.habitos.ui.screens.home.HomeScreen
import com.dezenbit.habitos.ui.screens.home.HomeViewModel
import com.dezenbit.habitos.ui.screens.reorder.ReorderHabitsScreen
import com.dezenbit.habitos.ui.screens.reorder.ReorderHabitsViewModel
import com.dezenbit.habitos.ui.screens.settings.AboutScreen
import com.dezenbit.habitos.ui.screens.settings.SettingsScreen
import com.dezenbit.habitos.ui.screens.settings.SettingsViewModel
import com.dezenbit.habitos.ui.screens.stats.StatisticsScreen
import com.dezenbit.habitos.ui.screens.stats.StatisticsViewModel

private object Routes {
    const val HOME = "home"
    const val STATS = "stats"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val REORDER = "reorder"
    const val ADD_EDIT = "add_edit?habitId={habitId}"
    const val DETAIL = "detail/{habitId}"

    fun addEdit(habitId: Long = -1L) = "add_edit?habitId=$habitId"
    fun detail(habitId: Long) = "detail/$habitId"
}

@Composable
fun HabitosNavGraph(
    application: Application,
    repository: HabitRepository,
    settingsRepository: SettingsRepository
) {
    val navController = rememberNavController()

    fun factory(habitId: Long = -1L) = ViewModelFactory(application, repository, settingsRepository, habitId)

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { androidx.compose.animation.EnterTransition.None },
        exitTransition = { androidx.compose.animation.ExitTransition.None },
        popEnterTransition = { androidx.compose.animation.EnterTransition.None },
        popExitTransition = { androidx.compose.animation.ExitTransition.None }
    ) {

        composable(Routes.HOME) {
            val vm: HomeViewModel = viewModel(factory = factory())
            HomeScreen(
                viewModel = vm,
                onAddHabit = { navController.navigate(Routes.addEdit()) },
                onOpenHabit = { id -> navController.navigate(Routes.detail(id)) },
                onOpenStats = { navController.navigate(Routes.STATS) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.STATS) {
            val vm: StatisticsViewModel = viewModel(factory = factory())
            StatisticsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onOpenHabit = { id -> navController.navigate(Routes.detail(id)) }
            )
        }

        composable(Routes.SETTINGS) {
            val vm: SettingsViewModel = viewModel(factory = factory())
            SettingsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onOpenReorder = { navController.navigate(Routes.REORDER) },
                onOpenAbout = { navController.navigate(Routes.ABOUT) }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.REORDER) {
            val vm: ReorderHabitsViewModel = viewModel(factory = factory())
            ReorderHabitsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ADD_EDIT,
            arguments = listOf(navArgument("habitId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getLong("habitId") ?: -1L
            val vm: AddEditHabitViewModel = viewModel(factory = factory(habitId))
            AddEditHabitScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getLong("habitId") ?: -1L
            val vm: HabitDetailViewModel = viewModel(factory = factory(habitId))
            HabitDetailScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onEdit = { id ->
                    navController.navigate(Routes.addEdit(id)) {
                        popUpTo(Routes.DETAIL) { inclusive = true }
                    }
                }
            )
        }
    }
}

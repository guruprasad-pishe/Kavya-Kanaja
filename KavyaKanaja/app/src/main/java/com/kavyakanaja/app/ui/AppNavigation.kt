package com.kavyakanaja.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kavyakanaja.app.ui.screens.*
import com.kavyakanaja.app.viewmodel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onPoemClick = { poemId ->
                    navController.navigate(Screen.PoemDetail.createRoute(poemId))
                },
                onAllPoemsClick = { navController.navigate(Screen.AllPoems.route) },
                onPoetsCornerClick = { navController.navigate(Screen.PoetsCorner.route) },
                onSearchClick = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(
            route = Screen.PoemDetail.route,
            arguments = listOf(navArgument("poemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val poemId = backStackEntry.arguments?.getInt("poemId") ?: return@composable
            PoemDetailScreen(
                poemId = poemId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AllPoems.route) {
            AllPoemsScreen(
                viewModel = viewModel,
                onPoemClick = { poemId ->
                    navController.navigate(Screen.PoemDetail.createRoute(poemId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PoetsCorner.route) {
            PoetsCornerScreen(
                viewModel = viewModel,
                onPoetClick = { poetId ->
                    navController.navigate(Screen.PoetDetail.createRoute(poetId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PoetDetail.route,
            arguments = listOf(navArgument("poetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val poetId = backStackEntry.arguments?.getInt("poetId") ?: return@composable
            PoetDetailScreen(
                poetId = poetId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                viewModel = viewModel,
                onPoemClick = { poemId ->
                    navController.navigate(Screen.PoemDetail.createRoute(poemId))
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

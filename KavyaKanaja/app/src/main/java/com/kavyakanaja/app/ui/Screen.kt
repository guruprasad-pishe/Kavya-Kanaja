package com.kavyakanaja.app.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PoemDetail : Screen("poem_detail/{poemId}") {
        fun createRoute(poemId: Int) = "poem_detail/$poemId"
    }
    object AllPoems : Screen("all_poems")
    object PoetsCorner : Screen("poets_corner")
    object PoetDetail : Screen("poet_detail/{poetId}") {
        fun createRoute(poetId: Int) = "poet_detail/$poetId"
    }
    object Search : Screen("search")
}

package com.nilesh.practiceapps.compose_route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nilesh.practiceapps.network.ResponseClass
import com.nilesh.practiceapps.ui.composeviews.UserDetailScreen
import com.nilesh.practiceapps.ui.composeviews.UserListScreen

object Routes {

    const val USER_LIST_SCREEN = "user_list_screen"
    const val USER_LIST_DETAIL_SCREEN = "user_list_detail_screen"

    @Composable
    fun AppNavHost(
        modifier: Modifier,
        navController: NavHostController,
        startDestination: String
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(USER_LIST_SCREEN) {
                UserListScreen(navController = navController)
            }

            composable(USER_LIST_DETAIL_SCREEN) {
                val userData: ResponseClass.UserData? =
                    navController.previousBackStackEntry?.savedStateHandle?.get("userListItem")
                userData?.let {
                    UserDetailScreen(
                        navController = navController,
                        userData = it
                    )
                }
            }
        }
    }
}
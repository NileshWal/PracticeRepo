package com.nilesh.practiceapps.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.nilesh.practiceapps.R
import com.nilesh.practiceapps.compose_route.Routes
import com.nilesh.practiceapps.ui.composeviews.TextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldView()
        }
    }

    @Composable
    fun ScaffoldView() {
        Scaffold(modifier = Modifier.fillMaxSize(),
            backgroundColor = Color(0xFFefefef),
            topBar = { TopAppBar(title = { TextView(text = LocalContext.current.getString(R.string.app_name)) }) },
            content = {
                Routes.AppNavHost(
                    modifier = Modifier.padding(it),
                    navController = rememberNavController(),
                    startDestination = Routes.USER_LIST_SCREEN
                )
            })
    }
}
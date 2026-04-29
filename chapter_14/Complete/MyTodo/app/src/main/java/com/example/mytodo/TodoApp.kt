package com.example.mytodo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mytodo.ui.navigation.TodoNavHost

@Composable
fun TodoApp(
  navController: NavHostController = rememberNavController(),
  modifier: Modifier = Modifier
) {
  TodoNavHost(navController = navController, modifier = modifier)
}
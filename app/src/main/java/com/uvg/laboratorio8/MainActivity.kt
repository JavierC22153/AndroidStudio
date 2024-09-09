package com.uvg.laboratorio8

import CharacterDb
import Character
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvg.laboratorio8.Characters.CharactersScreen
import com.uvg.laboratorio8.Characters.navigateToDetailScreen
import com.uvg.laboratorio8.Detail.DetailScreen
import com.uvg.laboratorio8.Detail.detailScreen
import com.uvg.laboratorio8.Login.LoginScreen
import com.uvg.laboratorio8.Login.navigateToCharactersScreen
import com.uvg.laboratorio8.ui.theme.Laboratorio8Theme

class MainActivity : ComponentActivity() {
    private val characterDb = CharacterDb()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio8Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login_screen",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable("login_screen") {
                            LoginScreen(
                                onLoginClick = {
                                    navController.navigate("characters_screen")
                                }
                            )
                        }
                        composable("characters_screen") {
                            CharactersScreen(
                                characterDb = characterDb,
                                onCharacterClick = { character ->
                                    navController.navigate("detail_screen/${character.id}")
                                }
                            )
                        }
                        composable("detail_screen/{characterId}") { backStackEntry ->
                            val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                            characterId?.let {
                                val character = characterDb.getCharacterById(it)
                                DetailScreen(character = character, onBackClick = {
                                    navController.navigateUp()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
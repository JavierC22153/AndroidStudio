package com.uvg.laboratorio10.presentation.mainFlow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uvg.laboratorio10.presentation.mainFlow.character.CharacterNavGraph
import com.uvg.laboratorio10.presentation.mainFlow.character.characterGraph
import com.uvg.laboratorio10.presentation.mainFlow.location.locationsGraph
import com.uvg.laboratorio10.presentation.mainFlow.profile.profileScreen
import com.uvg.laboratorio10.presentation.navigation.BottomNavBar
import com.uvg.laboratorio10.presentation.navigation.topLevelDestinations

@Composable
fun MainFlowScreen(
    onLogOutClick: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    var bottomBarVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    bottomBarVisible = if (currentDestination != null) {
        /*
            Utilizamos nuestro listado de destinos que deben mostrar el bottom bar y vemos si,
            nuestro destino actual (pantalla que estamos viendo), forma parte de ese listado.
            "any" es una función que recorre internamente el listado y, si al menos 1 de esos
            elementos retorna "hasRoute" como true, va a retornar todo true.
         */
        topLevelDestinations.any { destination ->
            /*
                Función de compose que compara si el destination actual (pantalla que estamos viendo)
                es igual al destination que estamos evaluando en la función "any".
             */
            currentDestination.hasRoute(destination)
        }
    } else {
        false
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Composable para animar cuando mostramos u ocultamos BottomBar
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavBar(
                    /*
                        Lambda que se usará internamente. Evalúa si, el elemento del bottom bar,
                        específicamente su destination, forma parte de cualquiera de los destinations
                        que forman parte de la jerarquía del destino actual. La jerarquía del destino actual
                        se refiere a cualquiera de las pantallas pertenecientes al grafo de navegación.
                        Por ejemplo, si estamos en CharacterProfile, la jerarquía sería:
                        CharacterNavGraph -> CharacterListDestination -> CharacterProfileDestination.
                        Si el destino actual es cualquiera de esos, entonces nuestro ejemplo retornaría
                        true.
                     */
                    checkItemSelected = { destination ->
                        currentDestination?.hierarchy?.any { it.hasRoute(destination::class) } ?: false
                    },
                    onNavItemClick = { destination ->
                        navController.navigate(destination) {
                            /*
                                Estas líneas nos permiten lograr lo siguiente:
                                * No almacenamos múltiples instancias de la misma pantalla en
                                nuestro backstack
                                * Si estamos en Characters y hacemos click en ese elemento nuevamente,
                                no creará una nueva pantalla
                                * Si regresamos a Characters luego de estar en Locations, el estado
                                de Characters será recuperado.
                             */
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharacterNavGraph,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            characterGraph(navController)
            locationsGraph(navController)
            profileScreen(
                onLogOutClick = onLogOutClick
            )
        }
    }
}
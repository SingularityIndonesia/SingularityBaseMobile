package bottomsheetflow

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

interface BottomSheetFlowScope {
    @OptIn(ExperimentalMaterial3Api::class)
    fun route(
        route: String,
        dismissAble: Boolean = true,
        content: @Composable NavHostController.(SheetState) -> Unit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFlow(
    startDestination: String,
    onCancel: () -> Unit,
    builder: BottomSheetFlowScope.() -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        startDestination = startDestination
    ) {
        val scope = object : BottomSheetFlowScope {
            override fun route(
                route: String,
                dismissAble: Boolean,
                content: @Composable (NavHostController.(SheetState) -> Unit)
            ) {
                composable(
                    route
                ) {
                    val sheetState = rememberModalBottomSheetState()

                    ModalBottomSheet(
                        onDismissRequest = {
                            if (!navController.popBackStack()) {
                                onCancel.invoke()
                            }
                        },
                        sheetState = sheetState
                    ) {
                        content.invoke(navController, sheetState)
                    }
                }
            }
        }

        builder.invoke(scope)
    }
}
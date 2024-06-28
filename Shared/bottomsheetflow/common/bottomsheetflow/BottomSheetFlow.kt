package bottomsheetflow

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Configuration scope for the BottomSheetFlow.
 * @see BottomSheetFlow
 */
interface BottomSheetFlowScope {

    /**
     * Adding route to the flow.
     * @param route is the route id
     * @param onDismiss Return true = dismiss bottomsheet. Return false = prevent dismissal. When the user touch the gray area or drag the bottomsheet to the bottom with dismissal intention, the `onDismiss` event will be invoked.
     * @param content is the composable content.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun route(
        route: String,
        onDismiss: ()-> Boolean = { true },
        content: @Composable NavHostController.(SheetState) -> Unit
    )
}

/**
 * BottomSheet flow is a flow of bottom sheet. You can configure multiple bottomsheets and perform navigation to each bottomsheet.
 * @param startDestination first launch route.
 * @param onCancel When the user performs a back action (either through navigation or dismissal) and the navigation reaches the end of the stack, the flow will be canceled, and `onCancel` will be invoked.
 * @param configuration Configuration scope to configure the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFlow(
    startDestination: String,
    onCancel: () -> Unit,
    configuration: BottomSheetFlowScope.() -> Unit,
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
                onDismiss: () -> Boolean,
                content: @Composable() (NavHostController.(SheetState) -> Unit)
            ) {
                composable(
                    route
                ) {
                    val sheetState = rememberModalBottomSheetState(
                        confirmValueChange = { state ->
                            if (state == SheetValue.Hidden)
                                onDismiss.invoke()
                            else
                                true
                        }
                    )

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

        configuration.invoke(scope)
    }
}
package dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import bottomsheetflow.BottomSheetFlow
import common.StateSaver
import designsystem.ExtraLargePadding
import designsystem.LargePadding
import designsystem.component.ExtraLargeSpacing
import designsystem.component.MediumSpacing
import designsystem.component.PrimaryButton
import designsystem.component.SecondaryButton
import designsystem.component.TertiaryButton
import designsystem.component.TextLabel
import designsystem.component.TextTitle
import example.model.TodoID
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import system.designsystem.resources.Res
import system.designsystem.resources.groot


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Context.DashboardPane(
    gotoGroot: () -> Unit = {},
    gotoTodoDetail: (TodoID) -> Unit = {},
) {

    val tabs = listOf("Singularity", "Todo List")
    val tabNavController = rememberNavController()
    var showSheet by remember { mutableStateOf(false) }

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        tabNavController.addOnDestinationChangedListener { _, destination, _ ->
            selectedTabIndex =
                if (destination.route?.contains("todo") == true)
                    1
                else 0
        }
    }
    val stateSaver = remember {
        StateSaver()
    }

    Box(
        modifier = Modifier
            .imePadding()
    ) {

        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.mapIndexed { index, title ->
                    Tab(
                        modifier = Modifier.height(60.dp),
                        selected = index == selectedTabIndex,
                        onClick = {
                            when (index) {
                                0 -> tabNavController.navigate("prolog")
                                1 -> tabNavController.navigate("todo-list")
                            }
                        },
                    ) {
                        TextLabel(title)
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
            ) {
                DashboardTabNavigation(
                    navController = tabNavController,
                    stateSaver = stateSaver,
                    goToTodoDetail = gotoTodoDetail
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(
                    vertical = ExtraLargePadding + LargePadding,
                    horizontal = LargePadding
                ),
            onClick = {
                // gotoGroot.invoke()
                showSheet = true
            }
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(Res.drawable.groot),
                contentDescription = "Groot customer service"
            )
        }
    }

    if (showSheet)
        BottomSheetInput(
            onCancel = {
                showSheet = false
            },
            onFinish = {
                showSheet = false
            }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetInput(
    onCancel: () -> Unit,
    onFinish: () -> Unit
) {
    BottomSheetFlow(
        startDestination = "input1",
        onCancel = onCancel
    ) {
        route(
            "input1"
        ) { sheetState ->
            Sheet1(
                sheetState,
                onBack = onCancel,
                onNext = {
                    navigate("input2")
                },
                onCancel = onCancel
            )
        }

        route(
            "input2"
        ) { sheetState ->
            Sheet2(
                sheetState,
                onBack = ::popBackStack,
                onNext = {
                    navigate("input3")
                },
                onCancel = onCancel
            )
        }

        route(
            "input3"
        ) { sheetState ->
            Sheet3(
                sheetState,
                onBack = ::popBackStack,
                onFinish = onFinish,
                onCancel = onCancel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sheet1(
    sheetState: SheetState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column {
        TextTitle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "Input 1"
        )
        ExtraLargeSpacing()
        Row {
            TertiaryButton(
                modifier = Modifier.weight(1f),
                label = "Cancel"
            ) {
                scope.launch {
                    sheetState.hide()
                    onCancel.invoke()
                }
            }
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back"
            ) {
                scope.launch {
                    sheetState.hide()
                    onBack.invoke()
                }
            }
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Next"
            ) {
                scope.launch {
                    sheetState.hide()
                    onNext.invoke()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sheet2(
    sheetState: SheetState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column {
        TextTitle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "Input 2"
        )
        ExtraLargeSpacing()
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TertiaryButton(
                modifier = Modifier.weight(1f),
                label = "Cancel"
            ) {
                scope.launch {
                    sheetState.hide()
                    onCancel.invoke()
                }
            }
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back"
            ) {
                scope.launch {
                    sheetState.hide()
                    onBack.invoke()
                }
            }
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Next"
            ) {
                scope.launch {
                    sheetState.hide()
                    onNext.invoke()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sheet3(
    sheetState: SheetState,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column {
        TextTitle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "Input 1"
        )
        ExtraLargeSpacing()
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TertiaryButton(
                modifier = Modifier.weight(1f),
                label = "Cancel"
            ) {
                scope.launch {
                    sheetState.hide()
                    onCancel.invoke()
                }
            }
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back"
            ) {
                scope.launch {
                    sheetState.hide()
                    onBack.invoke()
                }
            }
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Finish"
            ) {
                scope.launch {
                    sheetState.hide()
                    onFinish.invoke()
                }
            }
        }
    }
}
/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package bottomsheetflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import designsystem.component.ExtraLargeSpacing
import designsystem.component.MediumSpacing
import designsystem.component.PrimaryButton
import designsystem.component.SecondaryButton
import designsystem.component.TertiaryButton
import designsystem.component.TextTitle
import kotlinx.coroutines.launch
import simpleactivity.SimpleActivity

class BottomSheetFlowPreviewActivity : SimpleActivity() {

    @Composable
    override fun App() {
        var showSheet by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            PrimaryButton(
                label = "Open Bottom Sheet",
                modifier = Modifier.align(Alignment.Center),
            ) {
                showSheet = true
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetInput(
    onCancel: () -> Unit,
    onFinish: () -> Unit
) {
    val scope = rememberCoroutineScope()

    BottomSheetFlow(
        startDestination = "input1",
        onCancel = onCancel
    ) {
        route(
            route = "input1",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
        ) { sheetState ->
            Sheet1(
                onBack = {
                    scope.launch {
                        sheetState.hide()
                        onCancel.invoke()
                    }
                },
                onNext = {
                    scope.launch {
                        sheetState.hide()
                        navigate("input2")
                    }
                },
                onCancel = {
                    scope.launch {
                        sheetState.hide()
                        onCancel.invoke()
                    }
                }
            )
        }

        route(
            "input2",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
        ) { sheetState ->
            Sheet2(
                onBack = {
                    scope.launch {
                        sheetState.hide()
                        popBackStack()
                    }
                },
                onNext = {
                    scope.launch {
                        sheetState.hide()
                        navigate("input3")
                    }
                },
                onCancel = {
                    scope.launch {
                        sheetState.hide()
                        onCancel.invoke()
                    }
                }
            )
        }

        route(
            "input3",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
        ) { sheetState ->
            Sheet3(
                onBack = {
                    scope.launch {
                        sheetState.hide()
                        popBackStack()
                    }
                },
                onFinish = {
                    scope.launch {
                        sheetState.hide()
                        onFinish.invoke()
                    }
                },
                onCancel = {
                    scope.launch {
                        sheetState.hide()
                        onCancel.invoke()
                    }
                }
            )
        }
    }
}

@Composable
fun Sheet1(
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    Column {
        TextTitle(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "Input 1"
        )
        ExtraLargeSpacing()
        Row {
            TertiaryButton(
                modifier = Modifier.weight(1f),
                label = "Cancel",
                onClick = onCancel
            )
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back",
                onClick = onBack
            )
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Next",
                onClick = onNext
            )
        }
        ExtraLargeSpacing()
    }
}

@Composable
fun Sheet2(
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
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
                label = "Cancel",
                onClick = onCancel
            )
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back",
                onClick = onBack
            )
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Next",
                onClick = onNext
            )
        }
        ExtraLargeSpacing()
    }
}

@Composable
fun Sheet3(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
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
                label = "Cancel",
                onClick = onCancel
            )
            MediumSpacing()
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = "Back",
                onClick = onBack
            )
            MediumSpacing()
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Finish",
                onClick = onFinish
            )
        }
        ExtraLargeSpacing()
    }
}
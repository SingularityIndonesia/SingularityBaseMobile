/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bottomsheetflow.BottomSheetFlow
import designsystem.component.ExtraLargeSpacing
import designsystem.component.MediumSpacing
import designsystem.component.PrimaryButton
import designsystem.component.SecondaryButton
import designsystem.component.TertiaryButton
import designsystem.component.TextTitle
import kotlinx.coroutines.launch

class BottomSheetFlowPreview : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Composable
fun App() {

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
            route = "input1",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
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
            "input2",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
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
            "input3",
            onDismiss = {
                // you can show dialog here
                // showDismissDialog = true
                false
            }
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
        ExtraLargeSpacing()
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
        ExtraLargeSpacing()
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
        ExtraLargeSpacing()
    }
}
package ai_chat

import ai_chat.entity.ChatHistoryItemDisplay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import common.getPlatform
import common.isAndroid
import common.isIOS
import core.lifecycle.StateSaver
import designsystem.LargePadding
import designsystem.MediumPadding
import designsystem.component.LargeSpacing
import designsystem.component.MediumSpacing
import designsystem.component.TextBody
import designsystem.component.TextLabel
import designsystem.component.TextTitle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import system.designsystem.resources.Res
import system.designsystem.resources.groot

@Immutable
data class AIChatPanePld(
    val unit: Unit = Unit
)

@Composable
fun Context.AIChatPane(
    pld: AIChatPanePld,
    stateSaver: StateSaver,
    onBack: () -> Unit
) {
    val platform = remember { getPlatform() }
    val vm = viewModel<AIChatPaneViewModel>(
        factory = viewModelFactory {
            initializer {
                AIChatPaneViewModel(
                    context = this@AIChatPane,
                    defaultSate = stateSaver.pop() ?: AIChatPaneState.SaveAble()
                )
            }
        }
    )
    val states = vm.reducer

    Column {
        val listState = rememberLazyListState()
        Row(
            modifier = Modifier.padding(
                start = MediumPadding,
                top = MediumPadding
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(Res.drawable.groot),
                contentDescription = "Groot customer service"
            )
            MediumSpacing()
            TextTitle("I am Groot!")
        }
        MediumSpacing()

        val chatHistoryItem by states.historyDisplayItems.collectAsState(listOf())
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer
                ),
            state = listState
        ) {
            item {
                MediumSpacing()
            }
            items(chatHistoryItem.size) { index ->
                val item = chatHistoryItem[index]
                PairChatBlock(historyItem = item)
                MediumSpacing()
            }
            item {
                LargeSpacing()
            }
        }

        LaunchedEffect(chatHistoryItem) {
            if (chatHistoryItem.isEmpty())
                return@LaunchedEffect

            listState.animateScrollToItem(chatHistoryItem.size - 1 + 1 /**because we have spacer**/)
        }

        MediumSpacing()

        var prompt by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier
                .padding(
                    horizontal = LargePadding
                )
                .fillMaxWidth(),
            value = prompt,
            placeholder = {
                TextLabel("Enter Prompt")
            },
            onValueChange = { prompt = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (prompt.isBlank()) return@KeyboardActions
                    vm.Post(Intent.Chat(prompt))
                    prompt = ""
                }
            )
        )

        when {
            platform.isAndroid() -> {
                LargeSpacing()
                LargeSpacing()
            }

            platform.isIOS() -> {
                LargeSpacing()
                LargeSpacing()
            }

            else -> {
                LargeSpacing()
            }
        }
    }
}

@Composable
fun PairChatBlock(
    historyItem: ChatHistoryItemDisplay
) {
    Column {
        ChatBlock(message = historyItem.message)
        MediumSpacing()
        ChatAnswerBlock(message = historyItem.answer)
    }
}

@Composable
fun ChatBlock(
    message: String
) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = LargePadding
            )
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            colors = CardDefaults.cardColors().copy(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            TextBody(
                text = message,
                modifier = Modifier
                    .padding(
                        horizontal = LargePadding,
                        vertical = MediumPadding
                    )
            )
        }
    }
}

@Composable
fun ChatAnswerBlock(
    message: String
) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = LargePadding
            )
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterStart),
            colors = CardDefaults.cardColors().copy(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            TextBody(
                text = message,
                modifier = Modifier
                    .padding(
                        horizontal = LargePadding,
                        vertical = MediumPadding
                    )
            )
        }
    }
}

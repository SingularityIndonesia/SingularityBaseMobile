import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import system.designsystem.SingularityTheme
import shared.common.getPlatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val topPadding = WindowInsets.safeDrawing
        .asPaddingValues()
        .calculateTopPadding()
        .let {
            if (getPlatform().isIOS())
                it.minus(8.dp)
            else
                it.minus(0.dp)
        }

    SingularityTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = topPadding
                    )
            ) {
                ExampleNavigation()
            }
        }
    }
}
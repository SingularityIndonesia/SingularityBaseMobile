import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.singularity.data.VmSuccess
import com.singularity.designsystem.SingularityTheme
import example.ExampleApp
import example.util.Greeting
import main.composeapp.generated.resources.Res
import main.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ExampleApp()
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Screen() {
    val greeting = remember {
        Greeting().greet()
    }
    val testMultiProject = remember {
        VmSuccess(greeting)
    }
    var showContent by remember {
        mutableStateOf(
            false
        )
    }

    SingularityTheme {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        showContent = !showContent
                    }
                ) {
                    Text(
                        text = "Click me!"
                    )
                }

                AnimatedVisibility(
                    showContent
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            null
                        )
                        Text(
                            text = "Test Multi Project:\n$testMultiProject",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
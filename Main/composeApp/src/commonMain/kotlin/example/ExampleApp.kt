package example

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.singularity.designsystem.SingularityTheme
import example.presentation.ExampleNavigation

@Composable
fun ExampleApp() {
    SingularityTheme {
        // A surface container using the 'background' color from the theme
        Surface {
            ExampleNavigation()
        }
    }
}
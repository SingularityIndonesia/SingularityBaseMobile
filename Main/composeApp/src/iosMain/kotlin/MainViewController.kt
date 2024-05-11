import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import system.core.context.MainContext
import system.core.context.WebRepositoryContext
import com.singularity.webrepository.webRepositoryContext

fun MainViewController() = ComposeUIViewController {
    val initMainContext = remember {
        MainContext.init(
            object : MainContext {
                override val webRepositoryContext: WebRepositoryContext by webRepositoryContext()
                /*override val storageContext: StorageContext by lazy { TODO() }*/
            }
        )
    }
    App()
}
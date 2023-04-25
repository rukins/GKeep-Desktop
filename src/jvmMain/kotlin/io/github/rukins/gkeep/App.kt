package io.github.rukins.gkeep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.rukins.gkeep.repository.SettingsRepository
import io.github.rukins.gkeep.repository.UserDataRepository
import io.github.rukins.gkeep.ui.MainMenu
import io.github.rukins.gkeep.ui.page.GreetingPage
import io.github.rukins.gkeep.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener

val minWindowWidth: Dp = 400.dp
val minWindowHeight: Dp = 600.dp

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
//    Tray(
//        icon = painterResource("images/img.png"),
//        menu = {
//            Item("Quit App", onClick = ::exitApplication)
//        }
//    )

    val scope = rememberCoroutineScope()

    val viewModel = remember { AppViewModel() }

//    val icon = BitmapPainter(useResource("logo/logo.png", ::loadImageBitmap))
    Window(
        onCloseRequest = {
            viewModel.onCloseApp()
            exitApplication()
        },
        title = "GKeep",
        onPreviewKeyEvent = {
            when {
                (it.isCtrlPressed && it.key == Key.Equals && it.type == KeyEventType.KeyUp) -> {
                    scope.launch { viewModel.onClickEditOrCreateNote() }
                    true
                }
                (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyUp) -> {
                    if (viewModel.showNoteEditingBox.value) {
                        scope.launch(Dispatchers.IO) { viewModel.onCreateOrUpdateNote() }
                    }
                    true
                }
                else -> false
            }
        },
        icon = painterResource("logo/logo.png")
    ) {
        window.minimumSize = Dimension(
            minWindowWidth.value.toInt(),
            minWindowHeight.value.toInt()
        )

        window.addWindowFocusListener(object : WindowFocusListener {
            override fun windowGainedFocus(e: WindowEvent) {
                println("GAINED")
//                scope.launch(Dispatchers.IO) { viewModel.onRefresh() }
            }

            override fun windowLostFocus(e: WindowEvent) {
                println("LOST")
//                scope.launch(Dispatchers.IO) { viewModel.onRefresh() }
            }
        })

        if (!SettingsRepository.fileExists() || !UserDataRepository.fileExists()) {
            viewModel.isUserLoggedIn.value = false
        } else {
            viewModel.loadUserDataFromStorageFiles()
        }

        App(viewModel)
    }
}

@Composable
private fun App(viewModel: AppViewModel) {
    if (viewModel.isUserLoggedIn.value) {
        MainMenu(viewModel)
    } else {
        GreetingPage(viewModel)
    }
}

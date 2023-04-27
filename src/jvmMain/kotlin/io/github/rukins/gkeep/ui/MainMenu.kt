package io.github.rukins.gkeep.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.rukins.gkeep.objects.icons.outlined.*
import io.github.rukins.gkeep.objects.theme.ColorSchemes
import io.github.rukins.gkeep.ui.page.NotePage
import io.github.rukins.gkeep.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainMenu(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = ColorSchemes.getColorSchemeByOrdinalAndTheme(
            viewModel.settings.themeOrdinal.value,
            viewModel.settings.enableDarkMode.value
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            CenterAlignedTopAppBar(
                title = {
                    Text("GKeep", modifier = Modifier.align(Alignment.Center))
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) { viewModel.onRefresh() }
                        }
                    ) {
                        Icon(Icons.Outlined.Refresh, "Refresh")
                    }
                    IconButton(
                        onClick = {
                            scope.launch { viewModel.onChangeTheme() }
                        }
                    ) {
                        if (viewModel.settings.enableDarkMode.value) {
                            Icon(Icons.Outlined.LightMode, "LightMode")
                        } else {
                            Icon(Icons.Outlined.DarkMode, "DarkMode")
                        }
                    }
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(Icons.Outlined.Settings, "Settings")
                    }
                },
                modifier = Modifier.requiredHeight(50.dp)
            )

            NavigationBar(viewModel)

            when (viewModel.selectedNavigationElement.value) {
                NavigationElement.NOTES,
                NavigationElement.ARCHIVE,
                NavigationElement.TRASH -> NotePage(viewModel)
                NavigationElement.LABELS -> {}
            }

            when (viewModel.appStatus.value) {
                AppStatus.REFRESHING ->
                    CircularProgressIndicator(Modifier.absoluteOffset(75.dp, 10.dp))
                AppStatus.NO_INTERNET_CONNECTION ->
                    Icon(Icons.Outlined.WifiOff, "WifiOff",
                        Modifier.absoluteOffset(75.dp, 15.dp),
                        MaterialTheme.colorScheme.outline
                    )
                AppStatus.UNSPECIFIED -> {}
            }

            if (viewModel.showNavigationBar.value) {
                FloatingActionButton(
                    modifier = Modifier
                        .absoluteOffset(maxWidth - 75.dp, maxHeight - 75.dp)
                        .clip(shape = RoundedCornerShape(50.dp)),
                    onClick = {
                        scope.launch { viewModel.onClickEditOrCreateNote() }
                    }
                ) {
                    Icon(Icons.Outlined.Edit, "Create new note")
                }
            }

            if (viewModel.showNoteEditingBox.value) {
                // to dim background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onClick {
                            scope.launch(Dispatchers.IO) { viewModel.onCreateOrUpdateNote() }
                        }
                        .alpha(0.6f)
                        .background(Color.Black)
                ) {}

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NoteEditingBox(
                        Modifier
                            .align(Alignment.Center)
                            .height(maxHeight.div(1.3f))
                            .width(maxWidth.div(1.1f)),
                        viewModel
                    )
                    NoteActions(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationBar(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    val icons = getNavigationIcons()

    NavigationRail (
        header = {
            IconButton(
                onClick = {
                    scope.launch { viewModel.onClickMenuButton() }
                }
            ) {
                Icon(Icons.Outlined.Menu, "Menu")
            }
        },
    ) {
        icons.forEach { (navElement, icon) ->
            AnimatedVisibility(
                visible = viewModel.showNavigationBar.value,
            ) {
                NavigationRailItem(
                    icon = { icon() },
                    selected = viewModel.selectedNavigationElement.value == navElement,
                    onClick = { viewModel.selectedNavigationElement.value = navElement },
                    label = { Text(navElement.label) },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

private fun getNavigationIcons() = mapOf<NavigationElement, @Composable (() -> Unit)>(
    Pair(NavigationElement.NOTES) { Icon(Icons.Outlined.Notes, NavigationElement.NOTES.label) },
    Pair(NavigationElement.LABELS) { Icon(Icons.Outlined.Label, NavigationElement.LABELS.label) },
    Pair(NavigationElement.ARCHIVE) { Icon(Icons.Outlined.Archive, NavigationElement.ARCHIVE.label) },
    Pair(NavigationElement.TRASH) { Icon(Icons.Outlined.Delete, NavigationElement.TRASH.label) }
)

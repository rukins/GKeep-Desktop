package io.github.rukins.gkeep.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.rukins.gkeep.objects.theme.ColorSchemes
import io.github.rukins.gkeep.viewmodel.AppViewModel

@Composable
fun GreetingPage(viewModel: AppViewModel) {
    MaterialTheme(
        colorScheme = ColorSchemes.getColorSchemeByOrdinalAndTheme(
            viewModel.settings.themeOrdinal.value,
            viewModel.settings.enableDarkMode.value
        )
    ) {
        Box() {

        }
    }
}
package io.github.rukins.gkeep.objects.mutable

import androidx.compose.runtime.mutableStateOf
import io.github.rukins.gkeep.objects.Settings

class MutableSettings(settings: Settings) {
    val email = mutableStateOf(settings.email)
    var currentVersion = settings.currentVersion
    var masterToken = settings.masterToken
    val themeOrdinal = mutableStateOf(settings.themeOrdinal)
    val enableDarkMode = mutableStateOf(settings.enableDarkMode)

    fun toSettings(): Settings {
        return Settings(
            email.value,
            currentVersion,
            masterToken,
            themeOrdinal.value,
            enableDarkMode.value
        )
    }
}
package io.github.rukins.gkeep.objects.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object ColorSchemes {
    private val LIGHT_SCHEME_1 = lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE9DDFF),
        onPrimaryContainer = Color(0xFF22005D),
        secondary = Color(0xFF625B71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1E192B),
        tertiary = Color(0xFF7E5260),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFD9E3),
        onTertiaryContainer = Color(0xFF31101D),
        error = Color(0xFFBA1A1A),
        errorContainer = Color(0xFFFFDAD6),
        onError = Color(0xFFFFFFFF),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF1C1B1E),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF1C1B1E),
        surfaceVariant = Color(0xFFE7E0EB),
        onSurfaceVariant = Color(0xFF49454E),
        outline = Color(0xFF7A757F),
        inverseOnSurface = Color(0xFFF4EFF4),
        inverseSurface = Color(0xFF313033),
        inversePrimary = Color(0xFFCFBCFF),
        surfaceTint = Color(0xFF6750A4),
        outlineVariant = Color(0xFFCAC4CF),
        scrim = Color(0xFF000000),
    )

    private val DARK_SCHEME_1 = darkColorScheme(
        primary = Color(0xFFCFBCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFF4F378A),
        onPrimaryContainer = Color(0xFFE9DDFF),
        secondary = Color(0xFFCBC2DB),
        onSecondary = Color(0xFF332D41),
        secondaryContainer = Color(0xFF4A4458),
        onSecondaryContainer = Color(0xFFE8DEF8),
        tertiary = Color(0xFFEFB8C8),
        onTertiary = Color(0xFF4A2532),
        tertiaryContainer = Color(0xFF633B48),
        onTertiaryContainer = Color(0xFFFFD9E3),
        error = Color(0xFFFFB4AB),
        errorContainer = Color(0xFF93000A),
        onError = Color(0xFF690005),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF1C1B1E),
        onBackground = Color(0xFFE6E1E6),
        surface = Color(0xFF1C1B1E),
        onSurface = Color(0xFFE6E1E6),
        surfaceVariant = Color(0xFF49454E),
        onSurfaceVariant = Color(0xFFCAC4CF),
        outline = Color(0xFF948F99),
        inverseOnSurface = Color(0xFF1C1B1E),
        inverseSurface = Color(0xFFE6E1E6),
        inversePrimary = Color(0xFF6750A4),
        surfaceTint = Color(0xFFCFBCFF),
        outlineVariant = Color(0xFF49454E),
        scrim = Color(0xFF000000),
    )

    private val LIGHT_SCHEME_2 = lightColorScheme(
        primary = Color(0xFF984061),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFD9E2),
        onPrimaryContainer = Color(0xFF3E001D),
        secondary = Color(0xFF74565F),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFD9E2),
        onSecondaryContainer = Color(0xFF2B151C),
        tertiary = Color(0xFF7C5635),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDCC1),
        onTertiaryContainer = Color(0xFF2E1500),
        error = Color(0xFFBA1A1A),
        errorContainer = Color(0xFFFFDAD6),
        onError = Color(0xFFFFFFFF),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFFBFF),
        onBackground = Color(0xFF201A1B),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF201A1B),
        surfaceVariant = Color(0xFFF2DDE1),
        onSurfaceVariant = Color(0xFF514347),
        outline = Color(0xFF837377),
        inverseOnSurface = Color(0xFFFAEEEF),
        inverseSurface = Color(0xFF352F30),
        inversePrimary = Color(0xFFFFB1C8),
        surfaceTint = Color(0xFF984061),
        outlineVariant = Color(0xFFD5C2C6),
        scrim = Color(0xFF000000),
    )

    private val DARK_SCHEME_2 = darkColorScheme(
        primary = Color(0xFFFFB1C8),
        onPrimary = Color(0xFF5E1133),
        primaryContainer = Color(0xFF7B2949),
        onPrimaryContainer = Color(0xFFFFD9E2),
        secondary =  Color(0xFFE3BDC6),
        onSecondary = Color(0xFF422931),
        secondaryContainer = Color(0xFF5A3F47),
        onSecondaryContainer = Color(0xFFFFD9E2),
        tertiary = Color(0xFFEFBD94),
        onTertiary = Color(0xFF48290B),
        tertiaryContainer = Color(0xFF623F20),
        onTertiaryContainer = Color(0xFFFFDCC1),
        error = Color(0xFFFFB4AB),
        errorContainer = Color(0xFF93000A),
        onError = Color(0xFF690005),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF201A1B),
        onBackground = Color(0xFFEBE0E1),
        surface = Color(0xFF201A1B),
        onSurface = Color(0xFFEBE0E1),
        surfaceVariant = Color(0xFF514347),
        onSurfaceVariant = Color(0xFFD5C2C6),
        outline = Color(0xFF9E8C90),
        inverseOnSurface = Color(0xFF201A1B),
        inverseSurface = Color(0xFFEBE0E1),
        inversePrimary = Color(0xFF984061),
        surfaceTint = Color(0xFFFFB1C8),
        outlineVariant = Color(0xFF514347),
        scrim = Color(0xFF000000),
    )

    private val lightSchemeList: List<ColorScheme> = listOf(
        LIGHT_SCHEME_1, LIGHT_SCHEME_2
    )
    private val darkSchemeList: List<ColorScheme> = listOf(
        DARK_SCHEME_1, DARK_SCHEME_2
    )

    fun getColorSchemeByOrdinalAndTheme(ordinal: Int, darkTheme: Boolean): ColorScheme {
        val i = ordinal - 1

        if (i > lightSchemeList.size && !darkTheme) return LIGHT_SCHEME_1
        if (i > darkSchemeList.size && darkTheme) return DARK_SCHEME_1
        return if (darkTheme) darkSchemeList[i] else lightSchemeList[i]
    }
}
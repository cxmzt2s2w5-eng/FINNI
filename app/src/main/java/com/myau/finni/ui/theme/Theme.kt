package com.myau.finni.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.myau.finni.Bg
import com.myau.finni.Danger
import com.myau.finni.Ink
import com.myau.finni.Line
import com.myau.finni.Mint
import com.myau.finni.Primary
import com.myau.finni.PrimarySoft

/**
 * Одна светлая тема с цветами Финни.
 * Раньше тема брала цвета из обоев телефона (dynamic color) и включала
 * тёмный режим — поэтому стандартные кнопки были серо-синими.
 */
private val FinniColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimarySoft,
    onPrimaryContainer = Primary,
    secondary = Mint,
    onSecondary = Color.White,
    tertiary = Color(0xFFF2B51F),
    background = Bg,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = PrimarySoft,
    onSurfaceVariant = Ink,
    outline = Line,
    error = Danger
)

@Composable
fun FINNITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FinniColors,
        typography = Typography,
        content = content
    )
}

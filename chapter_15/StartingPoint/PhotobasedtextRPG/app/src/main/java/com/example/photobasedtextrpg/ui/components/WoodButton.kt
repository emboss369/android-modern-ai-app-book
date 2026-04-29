package com.example.photobasedtextrpg.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

private val WoodButtonTextBrush = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFFFFF5E1),
        0.7f to Color(0xFFD5B586),
        1.0f to Color(0xFFBA996A)
    )
)

@Composable
fun WoodButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontSize: TextUnit = 16.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .alpha(if (enabled) 1f else 0.4f)
    ) {
        Image(
            painter = painterResource(R.drawable.button_wood_long),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            //modifier = Modifier.matchParentSize()
        )
        Text(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                brush = WoodButtonTextBrush,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.7f),
                    offset = Offset(2f, 2f),
                    blurRadius = 5f
                )
            ),
            //modifier = Modifier.padding( horizontal =24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WoodButtonPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            WoodButton(
                text = "地図を拾い上げて調べる",
                onClick = {},
                modifier = Modifier,
                fontSize = 20.sp
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun WoodButton2Preview() {
    PhotoBasedTextRPGTheme(
        content = {
            WoodButton(
                text = "地図を拾い上げて調べる",
                onClick = {},
                modifier = Modifier,
                fontSize = 32.sp
            )
        },
    )
}
@Preview(showBackground = true)
@Composable
fun WoodButton3Preview() {
    PhotoBasedTextRPGTheme(
        content = {
            WoodButton(
                text = "地図を拾い上げて調べる",
                onClick = {},
                modifier = Modifier.width(200.dp),
                fontSize = 12.sp
            )
        },
    )
}


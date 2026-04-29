package com.example.photobasedtextrpg.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

enum class Genre(val labelJa: String, val iconRes: Int) {
    FANTASY("ファンタジー", R.drawable.ico_fantasy),
    SF("SF",              R.drawable.ico_sf),
    HORROR("ホラー",      R.drawable.ico_horror),
    MYSTERY("ミステリー", R.drawable.ico_mistery)
}


@Composable
fun GenreSelector(
    selectedGenre: Genre?,
    onGenreSelected: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Genre.entries.forEach { genre ->
            val isSelected = genre == selectedGenre
            Image(
                painter = painterResource(genre.iconRes),
                contentDescription = genre.labelJa,
                modifier = Modifier
                    .padding(4.dp)
                    .size(86.dp)
                    .dropShadow()
                    .clickable { onGenreSelected(genre) }
                    .then(
                        if (isSelected) Modifier.border(
                            width = 2.dp,
                            color = Color(0xFFD5B586),
                            shape = RoundedCornerShape(8.dp)
                        ) else Modifier
                    )
                    .alpha(if (isSelected) 1f else 0.5f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenreSelectorPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            GenreSelector(
                selectedGenre = null,
                onGenreSelected = {}
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GenreSelectorSFPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            GenreSelector(
                selectedGenre = Genre.SF,
                onGenreSelected = {}
            )
        },
    )
}
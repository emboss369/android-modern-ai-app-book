package com.example.photobasedtextrpg.ui.game

import android.R.attr.enabled
import android.R.attr.label
import android.R.attr.singleLine
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.Genre
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

sealed interface GameUiState {
    data object Loading : GameUiState
    data class Playing(
        val stageName: String,
        val genre: Genre,             // ジャンル情報を追加
        val currentTurn: Int,         // 1〜5
        val narration: String,
        val choices: List<String>,    // 通常3つ。生成中は空リスト
        val isChoicesLoading: Boolean // 選択肢生成中フラグ
    ) : GameUiState
    data class Error(val message: String) : GameUiState
}

@Composable
fun GameScreen(
    uiState: GameUiState,
    onChoiceSelected: (String) -> Unit,
    onFreeInput: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 背景画像の設定
        if (uiState is GameUiState.Playing) {
            val backgroundRes = when (uiState.genre) {
                Genre.SF -> R.drawable.background_sf
                Genre.HORROR -> R.drawable.background_horror
                Genre.MYSTERY -> R.drawable.background_mistery
                else -> R.drawable.backgorund_fantasy
            }
            Image(
                painter = painterResource(backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().background(Color.White))
        }

        when (uiState) {
            is GameUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is GameUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
            is GameUiState.Playing -> {
                GameContent(
                    state = uiState,
                    onChoiceSelected = onChoiceSelected,
                    onFreeInput = onFreeInput
                )
            }
        }
    }
}

@Composable
private fun GameContent(
    state: GameUiState.Playing,
    onChoiceSelected: (String) -> Unit,
    onFreeInput: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. 上部ヘッダ (固定高さ 72.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(elevation = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.banner_middle),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .dropShadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        borderRadius = 0.dp,
                        blurRadius = 16.dp,
                        offsetY = 6.dp
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
            ) {
                // 舞台名テキスト（白色、24.sp、bold）
                Text(
                    text = state.stageName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 24.dp)
                )
                // ターンドット（●が5つ。currentTurn以下は白、それ以降はグレー）
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                ) {
                    repeat(5) { index ->
                        Text(
                            text = "●",
                            color = if (index < state.currentTurn) Color.White else Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // 2. ストーリーテキスト領域 (weight(1f) で残りを埋める)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.old_paper_large),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = state.narration,
                modifier = Modifier.padding(16.dp),
                fontSize = 30.sp,
                color = Color.Black,
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 1.2.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                )
            )
        }

        // 3. 選択肢領域 (固定または wrap_content)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.old_paper_medium),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Column(
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.isChoicesLoading) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .background(Color(0xABD5B586), shape = RoundedCornerShape(8.dp))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                } else {
                    state.choices.forEach { choice ->
                        WoodButton(
                            text = choice,
                            onClick = { onChoiceSelected(choice) },
                            modifier = Modifier
                                .fillMaxWidth()
                            ,
                            enabled = true,
                            fontSize = 22.sp
                        )
                    }
                }
                // 4. 自由入力欄 (固定)
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    label = { Text("自由に行動を入力...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    onFreeInput(inputText)
                                    inputText = ""
                                }
                            },
                            enabled = !state.isChoicesLoading && inputText.isNotBlank()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit_note_24px),
                                contentDescription = "送信"
                            )
                        }
                    },
                    singleLine = true,
                    enabled = !state.isChoicesLoading,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (inputText.isNotBlank()) {
                                onFreeInput(inputText)
                                inputText = ""
                            }
                        }
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPlayingPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            GameScreen(
                uiState = GameUiState.Playing(
                    stageName = "霧の古城",
                    genre = Genre.FANTASY,
                    currentTurn = 2,
                    narration = "あなたは薄暗い廊下に立っている。遠くから何かが近づく音が聞こえた。",
                    choices = listOf("剣を構えて待ち受ける", "物陰に隠れる", "声をかける"),
                    isChoicesLoading = false
                ),
                onChoiceSelected = {},
                onFreeInput = {}
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenLoadingChoicesPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            GameScreen(
                uiState = GameUiState.Playing(
                    stageName = "霧の古城",
                    genre = Genre.FANTASY,
                    currentTurn = 2,
                    narration = "あなたは薄暗い廊下に立っている。遠くから何かが近づく音が聞こえた。",
                    choices = emptyList(),
                    isChoicesLoading = true
                ),
                onChoiceSelected = {},
                onFreeInput = {}
            )
        },
    )
}

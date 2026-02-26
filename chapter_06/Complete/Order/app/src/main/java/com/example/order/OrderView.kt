package com.example.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.ui.theme.Green500
import com.example.order.ui.theme.LightGreen100
import com.example.order.ui.theme.SkyBlue400
import com.example.order.ui.theme.Teal200

private val GradientStart = SkyBlue400
private val GradientEnd = Green500
private val SectionBackground = Color.White
private val AccentGreen = Green500

@Composable
fun OrderSectionCard(
  content: @Composable () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(SectionBackground, shape = RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    content()
  }
}

@Preview(showBackground = true , backgroundColor = 0xFF000000)
@Composable
fun OrderSectionCardPreview() {
  Box(modifier = Modifier.padding(10.dp)) {
    OrderSectionCard {
      Text("Sample Content")
    }
  }
}

@Composable
fun RadioButtonWithText(
  text: String,
  selected: Boolean,
  onSelect: () -> Unit
) {
  Row(
    modifier = Modifier
      .padding(4.dp)
      .fillMaxWidth()
      .selectable(selected = selected, onClick = onSelect),  // ❶
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selected,
      onClick = null,                                        // ❷
      colors = RadioButtonDefaults.colors(
        selectedColor = AccentGreen
      )
    )
    Text(text = text, style = MaterialTheme.typography.bodyLarge)
  }
}

@Preview(showBackground = true)
@Composable
fun RadioButtonWithTextPreview() {
  RadioButtonWithText(
    text = "Option 1",
    selected = true,
    onSelect = {}
  )
}

@Composable
fun MainDishSection() {
  var selectedDish by remember { mutableStateOf("ハンバーガー") }
  OrderSectionCard {
    Column(
      Modifier
        .fillMaxWidth()
        .selectableGroup()
    ) {
      Text("メインを選択",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold)
      RadioButtonWithText(
        text = "ハンバーガー",
        selected = selectedDish == "ハンバーガー"
      ) { selectedDish = "ハンバーガー" }
      RadioButtonWithText(
        text = "チーズバーガー",
        selected = selectedDish == "チーズバーガー"
      ) { selectedDish = "チーズバーガー" }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainDishSectionPreview() {
  MainDishSection()
}

@Composable
fun SideMenuSection() {
  var frenchFries by remember { mutableStateOf(false) }  // ❶

  OrderSectionCard {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text("サイドメニュー",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold)
      Row(
        modifier = Modifier.toggleable(  // ❷
          value = frenchFries,
          onValueChange = { frenchFries = it }
        ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Checkbox(
          checked = frenchFries,
          onCheckedChange = null,  // ❸
          colors = CheckboxDefaults.colors(checkedColor = AccentGreen)
        )
        Text("フレンチフライ", style = MaterialTheme.typography.bodyLarge)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SideMenuSectionPreview() {
  SideMenuSection()
}

@Composable
fun SauceAmountSection() {
  var sauceAmount by remember { mutableStateOf(0f) }  // ❶
  OrderSectionCard {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text("ソースの量を調整できます",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold)
      Slider(
        value = sauceAmount,
        onValueChange = { sauceAmount = it },  // ❷
        modifier = Modifier
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .fillMaxWidth(),
        colors = SliderDefaults.colors(
          thumbColor = AccentGreen,
          activeTrackColor = AccentGreen,
          inactiveTrackColor = Teal200
        )
      )
      Text(
        text = when {                              // ❸
          sauceAmount < 0.3f -> "少なめ"
          sauceAmount > 0.7f -> "多め"
          else -> "普通"
        },
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        color = AccentGreen,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SauceAmountSectionPreview() {
  SauceAmountSection()
}

@Composable
fun DrinkSelectionSection() {
  var expanded by remember { mutableStateOf(false) }        // ❶
  var selectedDrink by remember { mutableStateOf("選択してください") }  // ❷
  val drinks = listOf("アイスコーヒー", "アイスカフェオレ", "コーラ")    // ❸
  OrderSectionCard {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text("ドリンクを選択してください",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold)
      Box(
        modifier = Modifier.padding(top = 12.dp).fillMaxWidth()
      ) {
        OutlinedTextField(
          value = selectedDrink,
          onValueChange = { },
          modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },  // ❹
          trailingIcon = {
            Icon(
              Icons.Filled.ArrowDropDown, "dropdown",
              Modifier.clickable { expanded = true },
              tint = AccentGreen)            // ❺
          },
          label = { Text("ドリンク") },
          readOnly = true,                   // ❻
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentGreen,
            focusedLabelColor = AccentGreen,
          )
        )
        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false }  // ❼
        ) {
          drinks.forEach { drink ->
            DropdownMenuItem(
              text = { Text(text = drink) },
              onClick = {
                selectedDrink = drink   // ❽
                expanded = false        // ❾
              }
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DrinkSelectionSectionPreview() {
  DrinkSelectionSection()
}

@Composable
fun OrderButtonSection(text: String, onClick: () -> Unit = {}) {
  val gradient = Brush.linearGradient(
    colors = listOf(GradientStart, GradientEnd),
    start = Offset.Zero,
    end = Offset.Infinite
  )
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.Transparent  // ❶
    ),
    contentPadding = PaddingValues(),
    modifier = Modifier
      .fillMaxWidth()
      .background(gradient, shape = RoundedCornerShape(50))  // ❷
  ) {
    Text(
      text = text,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 40.sp
    )
  }
}

@Preview(showBackground = true)
@Composable
fun OrderButtonSectionPreview() {
  OrderButtonSection("注文する")
}

@Composable
fun OrderView(
  onTapButton: () -> Unit = {}
) {
  val scrollState = rememberScrollState()
  val backgroundGradient = Brush.linearGradient(
    colors = listOf(LightGreen100, Color.White),
    start = Offset(x = 600f, y = 0f),
    end = Offset(x = 300f, y = 900f)
  )
  val headerGradient = Brush.linearGradient(
    colors = listOf(GradientStart, GradientEnd),
    start = Offset.Zero,
    end = Offset.Infinite
  )

  Column(
    modifier = Modifier
      .background(brush = backgroundGradient)
      .padding(16.dp)
      .verticalScroll(scrollState),  // ❶
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = headerGradient,
          shape = RoundedCornerShape(16.dp))
        .padding(vertical = 24.dp, horizontal = 16.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "注文画面",
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 4.sp
      )
    }

    MainDishSection()
    SideMenuSection()
    SauceAmountSection()
    DrinkSelectionSection()
    OrderButtonSection("注文する", onClick = onTapButton)
  }
}

@Preview(showBackground = true,
  widthDp = 400, heightDp = 800,
  backgroundColor = 0xFF000000)
@Composable
fun OrderViewPreview() {
  OrderView()
}



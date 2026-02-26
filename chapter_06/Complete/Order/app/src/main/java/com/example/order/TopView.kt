package com.example.order

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.ui.theme.Green500
import com.example.order.ui.theme.LightGreen100
import com.example.order.ui.theme.Lime600
import com.example.order.ui.theme.Orange900
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.SkyBlue400

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopView(onTapButton: () -> Unit) {
  Column(
    modifier = Modifier
      .verticalScroll(rememberScrollState())
  ) {
    Header(onTapButton = onTapButton)
    Column(
      modifier = Modifier
        .background(
          brush = Brush.linearGradient(
            colors = listOf(LightGreen100, Color.White),
            start = Offset(x = 600.dp.px, y = 0.dp.px),
            end = Offset(x = 300.dp.px, y = 300.dp.px)
          )
        ),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      val scrollState = rememberScrollState()
      val menuItems = listOf(
        MenuItem(
          "クラシックバーガー",
          "定番のおいしさ。迷ったらまずはこちら。",
          "¥980",
          R.drawable.classicbeef
        ),
        MenuItem(
          "スパイシーチキン",
          "ピリ辛ソースが食欲をそそる一品。",
          "¥880",
          R.drawable.spicychicken
        ),
        MenuItem(
          "ベジタリアンバーガー",
          "新鮮野菜を贅沢に使用したヘルシーバーガー。",
          "¥920",
          R.drawable.vegetarian
        ),
        MenuItem(
          "バーベキューバーガー",
          "特製BBQソースと厚切りベーコンが絶妙。",
          "¥1,100",
          R.drawable.barbecue
        ),
        MenuItem(
          "ホットドッグ",
          "ジューシーなソーセージの定番ホットドッグ。",
          "¥650",
          R.drawable.hotdog
        ),
        MenuItem(
          "こだわりコーヒー",
          "厳選された豆を使用した香り高い一杯。",
          "¥450",
          R.drawable.coffee
        ),
        MenuItem(
          "チキンナゲット",
          "外はカリッと、中はジューシー。",
          "¥400",
          R.drawable.nugget
        )
      )

      Box(
        modifier = Modifier.padding(horizontal = 10.dp)
      ) {
        Text(
          text = "シンプルで、\n心地よいバーガーを。",
          modifier = Modifier
            .fillMaxWidth(),
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily.SansSerif,
          fontSize = 30.sp
        )
      }
      Box(
        modifier = Modifier.padding(horizontal = 10.dp)
      ) {
        Text(
          text = "余計なものは加えず、素材の味をまっすぐに。\n" +
              "最初のひと口から食べ終わるまで、気持ちよく楽しめるバーガーを提供しています。",
          modifier = Modifier
            .fillMaxWidth(),
          fontWeight = FontWeight.Thin,
          fontFamily = FontFamily.SansSerif,
          fontSize = 12.sp,
          color = Color.Gray
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 10.dp)
      ) {
        OnlineOrderButton(
          text = "オンライン注文",
          modifier = Modifier,
          onClick = { onTapButton() }
        )
        OutlinedButton(
          modifier = Modifier.height(38.dp),
          onClick = { }) {
          Text(
            "メニューを見る",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        }
      }

      FlowRow(
        Modifier.fillMaxWidth(1f).wrapContentHeight().padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
      ) {
        CapsuleBox(icon = "⏱", text = "最短10分で提供")
        CapsuleBox(icon = "🥬", text = "新鮮野菜を毎日仕入れ")
        CapsuleBox(icon = "☕", text = "コーヒー同時注文可能")
      }

      ShadowCard(modifier = Modifier.padding(horizontal = 10.dp)) {
        Image(
          painter = painterResource(id = R.drawable.burgershop),
          contentDescription = "topburger"
        )
      }

      Text(
        "私たちのこだわり",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)
      )

      InfoCard(
        modifier = Modifier.padding(10.dp),
        title = "素材の良さを活かすこと",
        description = "新鮮な食材を選び、シンプルな組み合わせで仕上げています。 素材そのものの味を大切にしています。"
      )
      InfoCard(
        modifier = Modifier.padding(10.dp),
        title = "最後までおいしく",
        description = "重たすぎず、食べ終わったあとも心地よい。バランスの取れた味わいを目指しています。"
      )
      InfoCard(
        modifier = Modifier.padding(10.dp),
        title = "丁寧に、清潔に",
        description = "調理から提供まで、ひとつひとつ丁寧に。安心して食べられる環境づくりを心がけています。"
      )

      Text("メニュー", fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
      Row(modifier = Modifier.horizontalScroll(scrollState)) {
        menuItems.forEach { menuItem ->
          MenuView(
            modifier = Modifier.width(320.dp).height(150.dp).padding(10.dp),
            item = menuItem,
            onClick = { onTapButton() }
          )
        }
      }

      // 店舗情報
      Text("店舗情報", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp,
        modifier = Modifier.padding(top = 20.dp, start = 10.dp))
      StoreInfoCard(
        modifier = Modifier.padding(horizontal = 10.dp),
        title = "営業時間",
        content = {
          Text("11:00 - 21:00（L.O. 20:30）", fontSize = 15.sp)
          Text("定休日：火曜日", fontSize = 15.sp)
        }
      )
      StoreInfoCard(
        modifier = Modifier.padding(horizontal = 10.dp),
        title = "アクセス",
        content = {
          Text("〒000-0000 〇〇県〇〇市〇〇 1-2-3", fontSize = 15.sp)
          Text("最寄り：〇〇駅 徒歩5分", fontSize = 15.sp)
        }
      )
      StoreInfoCard(
        modifier = Modifier.padding(horizontal = 10.dp),
        title = "お問い合わせ",
        content = {
          Text("TEL：00-0000-0000", fontSize = 15.sp)
          Text("MAIL：info@example.com", fontSize = 15.sp)
        }
      )
    }
    Footer()
  }
}

@Preview(showBackground = true)
@Composable
fun TopViewPreview() {
  TopView(onTapButton = {})
}

@Stable
inline val Dp.px: Float @Composable get() {
    val density = LocalDensity.current
    return with(density) { this@px.toPx() }
}

@Preview(showBackground = true)
@Composable
fun ButtonVariantsPreview() {
  OrderTheme {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Button(onClick = {}) {
        Text("Button")
      }
      ElevatedButton(onClick = {}) {
        Text("ElevatedButton")
      }
      FilledTonalButton(onClick = {}) {
        Text("FilledTonalButton")
      }
      OutlinedButton(onClick = {}) {
        Text("OutlinedButton")
      }
      TextButton(onClick = {}) {
        Text("TextButton")
      }
    }
  }
}

@Composable
fun ShadowCard(
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White,
  content: @Composable () -> Unit  // ❶
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .border(                                               // ❷
        width = 0.5.dp, color = Color.LightGray,
        shape = RoundedCornerShape(20.dp)
      )
      .dropShadow(
        shape = RoundedCornerShape(20.dp),
        shadow = androidx.compose.ui.graphics.shadow.Shadow(
          radius = 10.dp,
          spread = 1.dp,
          color = Color.LightGray,
          offset = DpOffset(2.dp, 6.dp),
        )
      )
      .background(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
      )
      .padding(20.dp),
    contentAlignment = Alignment.Center
  ) {
    content()
  }
}

@Preview(showBackground = true)
@Composable
fun ShadowCardPreview() {
  OrderTheme {
    Box(modifier = Modifier.padding(16.dp)) {
      ShadowCard {
        Text("ShadowCard")
      }
    }
  }
}

@Composable
fun OnlineOrderButton(
  text: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val gradient = Brush.linearGradient(
    colors = listOf(SkyBlue400, Green500),
    start = Offset.Zero,
    end = Offset.Infinite
  )
  Button(
    onClick = onClick,
    modifier = modifier
      .height(38.dp)
      .background(gradient, shape = RoundedCornerShape(50)),
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.Transparent
    )
  ) {
    Text(
      text = text,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 14.sp
    )
  }
}

@Preview(showBackground = true)
@Composable
fun OnlineOrderButtonPreview() {
  OrderTheme {
    Box(modifier = Modifier.padding(16.dp)) {
      OnlineOrderButton(
        text = "Online Order",
        onClick = {}
      )
    }
  }
}

@Composable
fun Header(
  onTapButton: () -> Unit = {}
){
  Row(
    modifier = Modifier
      .fillMaxWidth().padding(10.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ){
    Text(
      text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Orange900)) {  // ❶
          append("Colbar's")
        }
        append(" burger")
      },
      color = Lime600,
      fontSize = 25.sp,
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = TextStyle(
        shadow = Shadow(
          color = Color.Black, blurRadius = 6f,
          offset = Offset(2f, 2f)
        )
      )
    )
    OnlineOrderButton(
      text = "注文へ進む",
      modifier = Modifier,
      onClick = {
        onTapButton()
      }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
  Header()
}

@Composable
fun CapsuleBox(
  icon: String,
  text: String
) {
  Box(
    modifier = Modifier
      .height(40.dp)
      .padding(2.dp)
      .clip(RoundedCornerShape(50))  // ❶
      .background(Color.White)
      .border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape)
      .padding(horizontal = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(icon,
        modifier = Modifier
          .width(18.dp).height(18.dp)
          .align(Alignment.CenterVertically)
          .clip(RoundedCornerShape(30))
          .background(Color.LightGray),
        textAlign = TextAlign.Center
      )
      Text(text, color = Color.Gray)
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun CapsuleBoxPreview() {
  FlowRow(
    Modifier
      .fillMaxWidth(1f)
      .wrapContentHeight(align = Alignment.Top)
      .padding(10.dp),
    horizontalArrangement = Arrangement.spacedBy(5.dp),
    verticalArrangement = Arrangement.spacedBy(5.dp),
  ) {
    CapsuleBox(icon = "⏱", text = "最短10分で提供")
    CapsuleBox(icon = "🥬", text = "新鮮野菜を毎日仕入れ")
    CapsuleBox(icon = "☕", text = "コーヒー同時注文可能")
  }
}
data class MenuItem(         // ❶
  val title: String,
  val subtitle: String,
  val price: String,
  @DrawableRes val id: Int
)

@Composable
fun MenuView(
  modifier: Modifier = Modifier,
  item: MenuItem,
  backgroundColor: Color = Color.White,
  onClick: (Int) -> Unit = {}
) {
  ShadowCard(
    modifier = modifier.clickable { onClick(item.id) },  // ❷
    backgroundColor = backgroundColor
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(id = item.id),
        contentDescription = item.title,
        modifier = Modifier.size(80.dp)
      )
      Spacer(modifier = Modifier.width(16.dp))
      Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
      ) {
        Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.subtitle, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = item.price, fontWeight = FontWeight.Bold, fontSize = 18.sp)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MenuViewPreview() {
  Box(modifier = Modifier.padding(20.dp)) {
    MenuView(
      modifier = Modifier
        .width(320.dp)
        .height(180.dp),
      item = MenuItem(
        title = "クラシックバーガー",
        subtitle = "定番の旨味。迷ったらまずはこちら。",
        price = "¥980",
        id = R.drawable.classicbeef
      ),
      onClick = {}
    )
  }
}
@Composable
fun InfoCard(
  title: String,
  description: String,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White
) {
  ShadowCard(modifier = modifier, backgroundColor = backgroundColor) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      Text(text = description, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
    }
  }
}



@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
  Box(modifier = Modifier.padding(16.dp)) {
    InfoCard(
      modifier = Modifier.padding(10.dp),
      title = "素材の良さを活かすこと",
      description = "新鮮な食材を選び、シンプルな組み合わせで仕上げています。 素材そのものの味を大切にしています。"
    )
  }
}

@Composable
fun StoreInfoCard(
  title: String,
  content: @Composable ColumnScope.() -> Unit,  // ❶
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White
) {
  ShadowCard(modifier = modifier.padding(vertical = 5.dp), backgroundColor = backgroundColor) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        content()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun StoreInfoCardPreview() {
  Box(modifier = Modifier.padding(16.dp)) {
    StoreInfoCard(
      modifier = Modifier.padding(horizontal = 10.dp),
      title = "営業時間",
      content = {
        Text("11:00 - 21:00（L.O. 20:30）", fontSize = 15.sp)
        Text("定休日：火曜日", fontSize = 15.sp)
      }
    )
  }
}

@Composable
fun Footer(){
  val gradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0EA5E9), Color(0xFF22C55E)),
    start = Offset.Zero,
    end = Offset.Infinite
  )
  Box(
    modifier = Modifier.fillMaxWidth()
      .background(brush = gradient)
  ){
    Text("Simple. Clean. Good burgers.",
      modifier = Modifier.padding(10.dp),
      color = Color.White,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
  Footer()
}

@Preview(showBackground = true, widthDp = 250)
@Composable
fun FontWeightPreview() {
  OrderTheme {
    val weights = listOf(
      "Thin" to FontWeight.Thin,
      "ExtraLight" to FontWeight.ExtraLight,
      "Light" to FontWeight.Light,
      "Normal" to FontWeight.Normal,
      "Medium" to FontWeight.Medium,
      "SemiBold" to FontWeight.SemiBold,
      "Bold" to FontWeight.Bold,
      "ExtraBold" to FontWeight.ExtraBold,
      "Black" to FontWeight.Black
    )
    Column {
      weights.forEach { (label, weight) ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(label)
          Text(
            text = "Colbar's burger",
            fontWeight = weight
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TextAlignPreview() {
  OrderTheme {
    val alignments = listOf(
      "Left" to TextAlign.Left,
      "Right" to TextAlign.Right,
      "Center" to TextAlign.Center,
      "Justify" to TextAlign.Justify,
      "Start" to TextAlign.Start,
      "End" to TextAlign.End
    )
    Column {
      alignments.forEach { (label, alignment) ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = label,
            modifier = Modifier.width(80.dp)
          )
          Text(
            text = "Colbar's burger",
            modifier = Modifier
              .weight(1f)
              .border(1.dp, Color.Red)
              .padding(4.dp),
            textAlign = alignment
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun FontFamilyPreview() {
  OrderTheme {
    val families = listOf(
      "Default" to FontFamily.Default,
      "SansSerif" to FontFamily.SansSerif,
      "Serif" to FontFamily.Serif,
      "Monospace" to FontFamily.Monospace,
      "Cursive" to FontFamily.Cursive
    )
    Column(modifier = Modifier.padding(16.dp)) {
      families.forEach { (label, family) ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = label,
            modifier = Modifier.width(100.dp),
            fontSize = 16.sp
          )
          Text(
            text = "Colbar's burger",
            fontFamily = family,
            fontSize = 18.sp
          )
        }
      }
    }
  }
}

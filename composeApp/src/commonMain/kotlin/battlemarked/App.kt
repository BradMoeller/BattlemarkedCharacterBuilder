package battlemarked

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Palette sampled by eye from the in-game screens.
private val Backdrop = Color(0xFFCFC7B6)
private val PanelBg = Color(0xFFD9D2C2)
private val ColumnBg = Color(0xFFDDD6C7)
private val Ink = Color(0xFF3A2E22)
private val InkSoft = Color(0xFF6B5E4C)
private val Plaque = Color(0xFF1C1B19)
private val PlaqueEdge = Color(0xFF55524B)
private val PlaqueText = Color(0xFFF2EBDA)
private val Bronze = Color(0xFF9A7B3C)
private val Gold = Color(0xFFE6B83A)
private val ScrollTop = Color(0xFFE6D3A3)
private val ScrollBottom = Color(0xFFD5BD84)
private val Numerals = listOf("I", "II", "III")

@Composable
fun App() {
    val initial = remember {
        parseAppState(readLocationHash())
            ?: AppState(AvailableClasses.first(), AvailableClasses.first().secondary, Build())
    }
    var heroClass by remember { mutableStateOf(initial.heroClass) }
    var secondary by remember { mutableStateOf(initial.secondary) }
    var build by remember { mutableStateOf(initial.build) }
    var view by remember { mutableStateOf(initial.view) }
    var overview by remember { mutableStateOf(initial.overview) }
    var editing by remember { mutableStateOf<Pair<ColumnKind, Int>?>(null) }
    var copied by remember { mutableStateOf(false) }
    val columns = heroClass.columns(secondary)

    // Keep the URL in sync so the address bar is always a shareable link.
    val hash = AppState(heroClass, secondary, build, view, overview).toHash()
    LaunchedEffect(hash) { replaceLocationHash(hash); copied = false }

    MaterialTheme {
        Column(
            Modifier.fillMaxSize().background(Backdrop).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                PlaqueButton("BUILDER", selected = view == View.Builder) { view = View.Builder }
                PlaqueButton("OVERVIEW", selected = view == View.Overview) { view = View.Overview }
                Spacer(Modifier.weight(1f))
                PlaqueButton(if (copied) "LINK COPIED" else "SHARE") {
                    copyToClipboard(currentUrl())
                    copied = true
                }
            }
            if (view == View.Overview) {
                OverviewScreen(subject = overview, onSubject = { overview = it })
                return@Column
            }
            Header(
                heroClass = heroClass,
                picked = build.picks.size,
                onClass = { heroClass = it; secondary = it.secondary; build = Build() },
                onReset = { build = Build() },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Secondary attribute:", color = Ink, fontWeight = FontWeight.Bold)
                // The primary is fixed to the class; only the secondary can change.
                Attribute.entries.filter { it != heroClass.primary }.forEach { attr ->
                    PlaqueButton(attr.name.uppercase() + if (attr == heroClass.secondary) " (DEFAULT)" else "", selected = attr == secondary) {
                        if (attr != secondary) {
                            secondary = attr
                            build = build.clear(ColumnKind.Secondary)
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TraitsPanel(
                    columns = columns,
                    build = build,
                    onSlot = { kind, tier -> editing = kind to tier },
                    onReset = { build = Build() },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        editing?.let { (kind, tier) ->
            val col = columns.first { it.kind == kind }
            ChoiceDialog(
                title = "${col.title} - ${Numerals[tier]}",
                slot = col.slots[tier],
                picked = build.pick(kind, tier),
                onPick = { build = build.toggle(kind, tier, it); editing = null },
                onDismiss = { editing = null },
            )
        }
    }
}

@Composable
private fun Header(heroClass: HeroClass, picked: Int, onClass: (HeroClass) -> Unit, onReset: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(Modifier.weight(1f)) {
            Text(heroClass.name.uppercase(), fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Ink)
            Box(Modifier.width(260.dp).height(3.dp).background(Brush.horizontalGradient(listOf(Ink, Color.Transparent))))
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AvailableClasses.forEach { c ->
                    PlaqueButton(c.name.uppercase(), selected = c == heroClass) { onClass(c) }
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Skills", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Box(
                Modifier.size(56.dp).background(Plaque, CircleShape).border(3.dp, PlaqueEdge, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Text("$picked/9", color = Gold, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun PlaqueButton(text: String, selected: Boolean = true, onClick: () -> Unit) {
    Box(
        Modifier
            .background(if (selected) Plaque else Plaque.copy(alpha = 0.55f), RoundedCornerShape(4.dp))
            .border(BorderStroke(2.dp, PlaqueEdge), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) { Text(text, color = PlaqueText, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
}

@Composable
private fun TraitsPanel(
    columns: List<TraitColumn>,
    build: Build,
    onSlot: (ColumnKind, Int) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier,
) {
    Column(modifier.background(PanelBg, RoundedCornerShape(10.dp)).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("TRAITS", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.weight(1f))
            PlaqueButton("RESET", onClick = onReset)
        }
        Spacer(Modifier.height(12.dp))
        // Column headings.
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Spacer(Modifier.width(44.dp))
            columns.forEach { col ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(col.kind.label, color = InkSoft, fontSize = 14.sp)
                    Text(col.title, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        for (tier in 0..2) {
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(44.dp).background(Color(0xFFCDB98A), CircleShape).border(2.dp, Bronze, CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Text(Numerals[tier], fontWeight = FontWeight.Bold, color = Ink, fontSize = 16.sp) }
                columns.forEach { col ->
                    val trait = build.pick(col.kind, tier)?.let { col.slots[tier].options[it] }
                    SkillPlaque(trait, Modifier.weight(1f)) { onSlot(col.kind, tier) }
                }
            }
            if (tier < 2) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Spacer(Modifier.width(44.dp))
                    repeat(3) {
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { Chevron() }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillPlaque(trait: Trait?, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(4.dp)
    Column(
        modifier.heightIn(min = 72.dp)
            .background(Plaque, shape)
            .border(BorderStroke(if (trait != null) 2.dp else 3.dp, if (trait != null) Gold else PlaqueEdge), shape)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (trait != null) {
            Text(trait.name.uppercase(), color = PlaqueText, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
            Text(trait.description, color = PlaqueText.copy(alpha = 0.8f), fontSize = 11.sp, textAlign = TextAlign.Center)
        } else {
            Text("CHOOSE", color = PlaqueText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceDialog(title: String, slot: Slot, picked: Int?, onPick: (Int) -> Unit, onDismiss: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.background(Backdrop, RoundedCornerShape(10.dp)).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(title.uppercase(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Ink)
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                slot.options.forEachIndexed { i, trait ->
                    ScrollCard(trait, selected = picked == i, modifier = Modifier.weight(1f)) { onPick(i) }
                }
            }
            PlaqueButton("CANCEL", onClick = onDismiss)
        }
    }
}

@Composable
private fun ScrollCard(trait: Trait, selected: Boolean, modifier: Modifier, minHeight: Dp = 220.dp, onClick: () -> Unit) {
    val shape = RoundedCornerShape(topStart = 4.dp, topEnd = 10.dp, bottomEnd = 4.dp, bottomStart = 10.dp)
    Column(
        modifier.heightIn(min = minHeight)
            .background(Brush.verticalGradient(listOf(ScrollTop, ScrollBottom)), shape)
            .border(BorderStroke(if (selected) 3.dp else 1.dp, if (selected) Plaque else Bronze), shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(trait.name.uppercase(), fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF15120E), textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth(0.8f).height(1.dp).background(Bronze.copy(alpha = 0.6f)))
        Spacer(Modifier.height(14.dp))
        Text(trait.description, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15120E), textAlign = TextAlign.Center)
    }
}

@Composable
private fun Chevron() {
    Canvas(Modifier.size(width = 22.dp, height = 12.dp)) {
        val w = 3.dp.toPx()
        drawLine(InkSoft, Offset(0f, 0f), Offset(size.width / 2, size.height), w, StrokeCap.Round)
        drawLine(InkSoft, Offset(size.width, 0f), Offset(size.width / 2, size.height), w, StrokeCap.Round)
    }
}

/** Every skill of one class or attribute at once: tiers I-III, three options each. */
@Composable
private fun OverviewScreen(subject: String, onSubject: (String) -> Unit) {
    val heroClass = AvailableClasses.firstOrNull { it.name == subject }
    val attribute = Attribute.entries.firstOrNull { it.name == subject }
    val slots = heroClass?.classSlots ?: AttributeSlots.getValue(attribute ?: Attribute.Strength)

    Text("OVERVIEW", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Ink)
    Text("Classes", color = InkSoft, fontSize = 14.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AvailableClasses.forEach { c -> PlaqueButton(c.name.uppercase(), selected = c.name == subject) { onSubject(c.name) } }
    }
    Text("Attributes", color = InkSoft, fontSize = 14.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Attribute.entries.forEach { a -> PlaqueButton(a.name.uppercase(), selected = a.name == subject) { onSubject(a.name) } }
    }
    Column(Modifier.fillMaxWidth().background(PanelBg, RoundedCornerShape(10.dp)).padding(16.dp)) {
        Text(subject.uppercase(), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(12.dp))
        slots.forEachIndexed { tier, slot ->
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(44.dp).background(Color(0xFFCDB98A), CircleShape).border(2.dp, Bronze, CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Text(Numerals[tier], fontWeight = FontWeight.Bold, color = Ink, fontSize = 16.sp) }
                slot.options.forEach { trait ->
                    ScrollCard(trait, selected = false, modifier = Modifier.weight(1f), minHeight = 110.dp) {}
                }
            }
        }
    }
}

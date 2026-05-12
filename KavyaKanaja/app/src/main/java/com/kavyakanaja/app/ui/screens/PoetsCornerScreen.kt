package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavyakanaja.app.data.model.Poet
import com.kavyakanaja.app.ui.theme.*
import com.kavyakanaja.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetsCornerScreen(
    viewModel: MainViewModel,
    onPoetClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val poets by viewModel.poets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ಕವಿಗಳ ಮೂಲೆ", fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary)
                        Text("Poet's Corner – Jnanpith Laureates",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(0.15f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null,
                            tint = GoldAccent, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Jnanpith Awardees", fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface)
                            Text("Karnataka's literary giants who won India's highest literary honor",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.65f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(poets) { poet ->
                PoetCard(poet = poet, onClick = { onPoetClick(poet.id) })
            }
        }
    }
}

@Composable
fun PoetCard(poet: Poet, onClick: () -> Unit) {
    val avatarColors = listOf(SaffronDeep, KannadaGreen, SkyBlue, LotusRed, GoldAccent, InkBrown)
    val color = avatarColors[poet.id % avatarColors.size]
    val initial = poet.nameKannada.firstOrNull()?.toString() ?: "K"

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = poet.nameKannada,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = poet.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Award badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldAccent.copy(0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Stars, contentDescription = null,
                            modifier = Modifier.size(11.dp), tint = GoldAccent)
                        Text(
                            text = poet.award,
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${poet.born} – ${poet.died}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = poet.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            Icon(Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetDetailScreen(
    poetId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val poets by viewModel.poets.collectAsState()
    val poet = poets.find { it.id == poetId } ?: return

    val avatarColors = listOf(SaffronDeep, KannadaGreen, SkyBlue, LotusRed, GoldAccent, InkBrown)
    val color = avatarColors[poet.id % avatarColors.size]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(poet.nameKannada, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                // Hero section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color.copy(0.1f))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(96.dp).clip(CircleShape).background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = poet.nameKannada.firstOrNull()?.toString() ?: "K",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(poet.nameKannada, style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(poet.name, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(20.dp), color = GoldAccent.copy(0.2f)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null,
                                tint = GoldAccent, modifier = Modifier.size(16.dp))
                            Text(poet.award, style = MaterialTheme.typography.labelMedium,
                                color = GoldAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${poet.born} – ${poet.died}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.55f))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("ಜೀವನ ಚರಿತ್ರೆ", style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text("Biography", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(poet.bioKannada, style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 24.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(poet.bio, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
                            fontStyle = FontStyle.Italic, lineHeight = 22.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("ಪ್ರಮುಖ ಕೃತಿಗಳು", style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text("Famous Works", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        Spacer(modifier = Modifier.height(10.dp))
                        poet.famousWorks.forEachIndexed { i, work ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(24.dp).clip(CircleShape)
                                        .background(color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${i + 1}", style = MaterialTheme.typography.labelSmall,
                                        color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Text(work, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

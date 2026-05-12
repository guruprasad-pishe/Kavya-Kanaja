package com.kavyakanaja.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavyakanaja.app.data.model.Poem
import com.kavyakanaja.app.ui.theme.*
import com.kavyakanaja.app.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onPoemClick: (Int) -> Unit,
    onAllPoemsClick: () -> Unit,
    onPoetsCornerClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val poemOfTheDay by viewModel.poemOfTheDay.collectAsState()
    val allPoems by viewModel.allPoems.collectAsState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ಕಾವ್ಯ ಕಣಜ",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Kavya Kanaja – Poetry Granary",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Poem of the Day
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically { -40 }
                ) {
                    poemOfTheDay?.let { poem ->
                        PoemOfTheDayCard(poem = poem, onClick = { onPoemClick(poem.id) })
                    }
                }
            }

            // Quick nav cards
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LibraryBooks,
                        label = "All Poems",
                        sublabel = "ಎಲ್ಲ ಕವಿತೆಗಳು",
                        color = SaffronDeep,
                        onClick = onAllPoemsClick
                    )
                    QuickNavCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Person,
                        label = "Poet's Corner",
                        sublabel = "ಕವಿಗಳ ಮೂಲೆ",
                        color = KannadaGreen,
                        onClick = onPoetsCornerClick
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Categories
            item {
                val categories = viewModel.getCategories()
                SectionHeader(title = "Browse by Category", kannada = "ವರ್ಗದ ಪ್ರಕಾರ")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            onClick = { /* filter by category */ }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Featured Poems
            item {
                SectionHeader(title = "Featured Poems", kannada = "ಆಯ್ದ ಕವಿತೆಗಳು")
            }

            items(allPoems.take(8)) { poem ->
                PoemListItem(poem = poem, onClick = { onPoemClick(poem.id) })
            }

            if (allPoems.size > 8) {
                item {
                    TextButton(
                        onClick = onAllPoemsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("View All ${allPoems.size} Poems →", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun PoemOfTheDayCard(poem: Poem, onClick: () -> Unit) {
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(
                Brush.linearGradient(
                    listOf(SaffronDeep, SaffronLight, GoldAccent)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ಇಂದಿನ ಕವಿತೆ",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Poem of the Day",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = today,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = poem.titleKannada,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = poem.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Show first 2 lines of Kannada verse
            Text(
                text = poem.verseKannada.lines().take(2).joinToString("\n"),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = poem.poetKannada,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = poem.era,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Read More",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickNavCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    sublabel: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Text(sublabel, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.8f))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, kannada: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = kannada,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, bottom = 4.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun CategoryChip(category: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PoemListItem(poem: Poem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SaffronDeep)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = poem.titleKannada,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = poem.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = poem.poetKannada,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("·", color = MaterialTheme.colorScheme.onSurface.copy(0.4f))
                    Text(
                        text = poem.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

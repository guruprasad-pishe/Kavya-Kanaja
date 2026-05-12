package com.kavyakanaja.app.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kavyakanaja.app.data.model.Poem
import com.kavyakanaja.app.data.model.WordMeaning
import com.kavyakanaja.app.ui.theme.*
import com.kavyakanaja.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemDetailScreen(
    poemId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val allPoems by viewModel.allPoems.collectAsState()
    val poem = allPoems.find { it.id == poemId }

    var selectedWord by remember { mutableStateOf<WordMeaning?>(null) }
    var showBhavartha by remember { mutableStateOf(false) }

    if (poem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = poem.titleKannada,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(SaffronDeep.copy(0.12f), Color.Transparent))
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = poem.titleKannada,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = poem.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoChip(label = poem.poetKannada, icon = Icons.Default.Person)
                        InfoChip(label = poem.category)
                        InfoChip(label = poem.era)
                    }
                }
            }

            // Divider with ornament
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = SaffronDeep.copy(0.3f))
                Text("  ❧  ", fontSize = 20.sp, color = SaffronDeep)
                Divider(modifier = Modifier.weight(1f), color = SaffronDeep.copy(0.3f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verse Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoStories, contentDescription = null, tint = SaffronDeep, modifier = Modifier.size(18.dp))
                        Text("ಕನ್ನಡ ಶ್ಲೋಕ", style = MaterialTheme.typography.labelLarge, color = SaffronDeep, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Tappable words
                    ClickableVerseText(
                        verse = poem.verseKannada,
                        words = poem.words,
                        onWordClick = { selectedWord = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = MaterialTheme.colorScheme.outline.copy(0.3f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(18.dp))
                        Text("English Transliteration", style = MaterialTheme.typography.labelLarge, color = SkyBlue, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = poem.verse,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        lineHeight = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bhavartha (Meaning) Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showBhavartha = !showBhavartha },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = KannadaGreen.copy(0.08f)
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                            Text("ಭಾವಾರ್ಥ – Bhavartha", style = MaterialTheme.typography.labelLarge, color = KannadaGreen, fontWeight = FontWeight.Bold)
                        }
                        Icon(
                            if (showBhavartha) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = KannadaGreen
                        )
                    }

                    AnimatedVisibility(visible = showBhavartha) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = poem.bhavarthaKannada,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = poem.bhavartha,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
                                lineHeight = 24.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    if (!showBhavartha) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Tap to reveal meaning",
                            style = MaterialTheme.typography.labelSmall,
                            color = KannadaGreen.copy(0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Word Bank
            if (poem.words.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = SaffronDeep, modifier = Modifier.size(18.dp))
                            Text("Word Meanings", style = MaterialTheme.typography.labelLarge, color = SaffronDeep, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("ಶಬ್ದಾರ್ಥಗಳು – Tap a word above to explore", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        Spacer(modifier = Modifier.height(12.dp))
                        poem.words.forEach { word ->
                            WordMeaningRow(wordMeaning = word)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Word meaning popup dialog
    selectedWord?.let { word ->
        WordMeaningDialog(wordMeaning = word, onDismiss = { selectedWord = null })
    }
}

@Composable
fun ClickableVerseText(
    verse: String,
    words: List<WordMeaning>,
    onWordClick: (WordMeaning) -> Unit
) {
    val wordSet = words.associateBy { it.word }
    val lines = verse.split("\n")

    Column {
        lines.forEach { line ->
            val lineWords = line.split(" ")
            val annotatedString = buildAnnotatedString {
                lineWords.forEachIndexed { index, token ->
                    val clean = token.trim()
                    val match = wordSet[clean]
                    if (match != null) {
                        pushStringAnnotation(tag = "WORD", annotation = clean)
                        withStyle(
                            SpanStyle(
                                color = SaffronDeep,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(token)
                        }
                        pop()
                    } else {
                        withStyle(SpanStyle(color = LocalContentColor.current)) {
                            append(token)
                        }
                    }
                    if (index < lineWords.size - 1) append(" ")
                }
            }
            ClickableText(
                text = annotatedString,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Normal
                ),
                onClick = { offset ->
                    annotatedString.getStringAnnotations("WORD", offset, offset)
                        .firstOrNull()?.let { ann ->
                            wordSet[ann.item]?.let { onWordClick(it) }
                        }
                }
            )
        }
    }
}

@Composable
fun WordMeaningDialog(wordMeaning: WordMeaning, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SaffronDeep),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = wordMeaning.word,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SaffronDeep,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = SaffronDeep.copy(0.3f))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = wordMeaning.meaningKannada,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = wordMeaning.meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Got it!", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun WordMeaningRow(wordMeaning: WordMeaning) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(0.4f)) {
            Text(
                text = wordMeaning.word,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SaffronDeep
            )
        }
        Column(modifier = Modifier.weight(0.6f)) {
            Text(
                text = wordMeaning.meaningKannada,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = wordMeaning.meaning,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun InfoChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.let {
                Icon(it, contentDescription = null, modifier = Modifier.size(11.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

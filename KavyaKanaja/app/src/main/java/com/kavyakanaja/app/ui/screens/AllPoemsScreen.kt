package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kavyakanaja.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPoemsScreen(
    viewModel: MainViewModel,
    onPoemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val allPoems by viewModel.allPoems.collectAsState()
    val categories = viewModel.getCategories()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val displayedPoems = if (selectedCategory == null) allPoems
    else viewModel.getPoemsByCategory(selectedCategory!!)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ಎಲ್ಲ ಕವಿತೆಗಳು", fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary)
                        Text("All Poems (${allPoems.size})",
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All") }
                        )
                    }
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }

            items(displayedPoems) { poem ->
                PoemListItem(poem = poem, onClick = { onPoemClick(poem.id) })
            }
        }
    }
}

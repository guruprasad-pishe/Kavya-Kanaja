package com.kavyakanaja.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kavyakanaja.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    onPoemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.search("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.search(it) },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        placeholder = { Text("Search poems, poets…") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.search("") }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.5f)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null,
                            tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        }
    ) { padding ->
        if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No poems found", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)
                    Text("Try a different keyword", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 0.dp)
            ) {
                if (searchQuery.isEmpty()) {
                    item {
                        Text(
                            "All Poems",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        )
                    }
                } else {
                    item {
                        Text(
                            "${searchResults.size} results for \"$searchQuery\"",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        )
                    }
                }
                items(searchResults) { poem ->
                    PoemListItem(poem = poem, onClick = { onPoemClick(poem.id) })
                }
            }
        }
    }
}

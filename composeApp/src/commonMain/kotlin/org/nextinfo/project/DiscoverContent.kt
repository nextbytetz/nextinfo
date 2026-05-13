package org.nextinfo.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DiscoverContent(
    paddingValues : PaddingValues,
    subscribed : Set<String>,
    onToggle : (String) -> Unit,
    onCompanyClick : (Company) -> Unit
) {
    var searchQuery  by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf("ALL") }

    val filteredList by remember(searchQuery, activeFilter) {
        derivedStateOf {
            tanzaniaCompanies.drop(FEATURED_COUNT).filter { company ->
                val matchFilter = activeFilter == "ALL" || company.category == activeFilter
                val q = searchQuery.lowercase()
                val matchSearch = q.isEmpty() || company.name.lowercase().contains(q) || company.tags.any { it.lowercase().contains(q) }
                matchFilter && matchSearch
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
        }
        item {
            FilterChipsRow(activeFilter = activeFilter, onFilterChange = { activeFilter = it })
        }

        item { SectionLabel("Spotlight") }
        item {
            FeaturedSlider(
                companies = tanzaniaCompanies,
                subscribed = subscribed,
                onToggle = onToggle,
                onCompanyClick = onCompanyClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(2.dp))
            StatsRow(
                totalCompanies  = tanzaniaCompanies.size,
                subscribedCount = subscribed.size
            )
        }

        item { SectionLabel("Discover Companies") }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No companies match your search.",
                        color = Gray600,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(filteredList, key = { it.name }) { company ->
                CompanyCard(
                    company = company,
                    isSubscribed = company.name in subscribed,
                    onToggle = { onToggle(company.name) },
                    onClick = { onCompanyClick(company) }
                )
            }
        }
    }
}
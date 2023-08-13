package com.timurkhabibulin.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.Tab
import com.timurkhabibulin.ui.uikit.TabIndicator
import kotlinx.coroutines.launch


@Preview
@Composable
internal fun SearchScreenPreview() {
    MysplashTheme {
        SearchScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(

) {
    val density = LocalDensity.current
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(SheetState(true, density))
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            Filter(
                Modifier.padding(bottom = 20.dp),
                onCancelClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.hide()
                    }
                },
                onApplyClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.hide()
                    }
                }
            )
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,
        content = {
            Scaffold(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background),
                floatingActionButton = {
                    FilterButton(onClick = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    })
                },
                floatingActionButtonPosition = FabPosition.Center
            ) { paddingValues ->
                val category = remember {
                    mutableStateOf(SearchCategory.PHOTOS)
                }

                Column(
                    Modifier
                        .padding(paddingValues)
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SearchBar(
                        onStartSearch = {
                               when (category.value) {
                                   SearchCategory.PHOTOS -> TODO()
                                   SearchCategory.COLLECTIONS -> TODO()
                                   SearchCategory.USERS -> TODO()
                               }
                        }
                    )
                    Tabs(currentCategory = category)
                    SearchContent()
                }
            }
        }
    )

}

@Composable
internal fun SearchContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LET'S FIND SOMETHING\n" +
                    " INCREDIBLE",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
internal fun Tabs(
    modifier: Modifier = Modifier,
    currentCategory: MutableState<SearchCategory>
//    tabs: List<String>
) {
    /* var selectedTabIndex by remember {
         mutableIntStateOf(0)
     }*/
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            15.dp, Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Category", style = MaterialTheme.typography.labelMedium)
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            selectedTabIndex = currentCategory.value.ordinal,
            indicator = { TabIndicator(tabPosition = it[currentCategory.value.ordinal]) },
            divider = {},
            edgePadding = 0.dp
        ) {
            SearchCategory.values().forEach { searchCategory ->
                Tab(
                    text = searchCategory.name,
                    selected = currentCategory.value == searchCategory,
                    onClick = { currentCategory.value = searchCategory }
                )
            }
            /*      tabs.forEachIndexed { index: Int, s: String ->
                      Tab(
                          text = s,
                          selected = selectedTabIndex == index,
                          onClick = { selectedTabIndex = index }
                      )
                  }*/
        }
    }
}

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    onStartSearch: (String) -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = {
            query = it
        },
        placeholder = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Find something",
                style = MaterialTheme.typography.titleSmall
            )
        },
        shape = RoundedCornerShape(50.dp),
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onStartSearch(query) },
                painter = painterResource(id = R.drawable.search_sm),
                contentDescription = ""
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        maxLines = 1,
    )
}
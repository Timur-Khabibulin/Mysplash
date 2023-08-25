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
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.CollectionCard
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalColumn
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoCard
import com.timurkhabibulin.ui.uikit.Tab
import com.timurkhabibulin.ui.uikit.TabIndicator
import com.timurkhabibulin.ui.uikit.UserPreviewCard
import kotlinx.coroutines.launch


@Preview
@Composable
internal fun SearchScreenPreview() {
    MysplashTheme {
        SearchScreen({}, {}, {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onCollectionClick: (String) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val searchQuery by searchScreenViewModel.searchQuery.collectAsState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FilterButton(onClick = {
                scope.launch {
                    bottomSheetState.expand()
                }
            })
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val category = rememberSaveable {
                mutableStateOf(SearchCategory.PHOTOS)
            }

            SearchBar(
                placeholder = searchQuery,
                onStartSearch = searchScreenViewModel::changeSearchQuery
            )
            Tabs(currentCategory = category)
            SearchContent(
                category = category,
                searchQuery = searchQuery,
                onPhotoClick = onPhotoClick,
                onUserClick = onUserClick,
                onCollectionClick = onCollectionClick,
                searchScreenViewModel = searchScreenViewModel
            )
        }

        BottomSheetFilter(
            modalBottomSheetState = bottomSheetState,
            defaultColor = searchScreenViewModel.color.value,
            defaultOrientation = searchScreenViewModel.orientation.value,
            onApplyClick = { color, orientation ->
                searchScreenViewModel.color.value = color
                searchScreenViewModel.orientation.value = orientation
                searchScreenViewModel.searchParametersChanged()
            }
        )
    }
}

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    onStartSearch: (String) -> Unit
) {
    var query by remember {
        mutableStateOf(placeholder)
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
                text = stringResource(R.string.find_something),
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
        keyboardActions = KeyboardActions(onSearch = {
            onStartSearch(query)
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
internal fun Tabs(
    modifier: Modifier = Modifier,
    currentCategory: MutableState<SearchCategory>
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            15.dp, Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = stringResource(R.string.category), style = MaterialTheme.typography.labelMedium)
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
        }
    }
}

@Composable
internal fun SearchContent(
    category: MutableState<SearchCategory>,
    searchQuery: String,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onCollectionClick: (String) -> Unit,
    searchScreenViewModel: SearchScreenViewModel
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (searchQuery.isNotEmpty()) {
            when (category.value) {
                SearchCategory.PHOTOS ->
                    PagingPullRefreshVerticalStaggeredGrid(
                        items = searchScreenViewModel.photos,
                        itemCard = {
                            PhotoCard(
                                photo = it,
                                onPhotoClick = onPhotoClick,
                                onUserClick = onUserClick
                            )
                        },
                        columns = StaggeredGridCells.Adaptive(300.dp),
                        verticalItemSpacing = 20.dp,
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                    )

                SearchCategory.COLLECTIONS ->
                    PagingPullRefreshVerticalColumn(
                        items = searchScreenViewModel.collections,
                        itemCard = { collection ->
                            CollectionCard(
                                collection = collection,
                                onClick = { onCollectionClick(it.id) }
                            )
                        },
                        space = 20.dp
                    )

                SearchCategory.USERS ->
                    PagingPullRefreshVerticalColumn(
                        items = searchScreenViewModel.users,
                        itemCard = {
                            UserPreviewCard(
                                user = it,
                                onUserClick = onUserClick,
                                onPhotoClick = onPhotoClick
                            )
                        },
                        space = 20.dp
                    )
            }
        } else {
            Text(
                text = stringResource(R.string.lets_find_something_incredible),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetFilter(
    modalBottomSheetState: SheetState,
    defaultColor: Color?,
    defaultOrientation: Orientation?,
    onApplyClick: (Color?, Orientation?) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (modalBottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = modalBottomSheetState,
        ) {
            Filter(
                Modifier.padding(bottom = 40.dp),
                defaultColor = defaultColor,
                defaultOrientation = defaultOrientation,
                onCancelClick = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onApplyClick = { color, orientation ->
                    scope.launch {
                        onApplyClick(color, orientation)
                        modalBottomSheetState.hide()
                    }
                }
            )
        }
    }
}
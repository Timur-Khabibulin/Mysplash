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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.core.analytics.AnalyticsAction
import com.timurkhabibulin.core.analytics.AnalyticsEvent
import com.timurkhabibulin.core.analytics.ContentType
import com.timurkhabibulin.core.utils.LocalAnalytics
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.search.filter.Filter
import com.timurkhabibulin.search.filter.FilterButton
import com.timurkhabibulin.search.filter.FilterHandler
import com.timurkhabibulin.search.filter.FilterUIState
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.CollectionCard
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalColumn
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoCard
import com.timurkhabibulin.ui.uikit.Tab
import com.timurkhabibulin.ui.uikit.TabIndicator
import com.timurkhabibulin.ui.uikit.UserPreviewCard
import kotlinx.coroutines.flow.Flow
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
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val analytics = LocalAnalytics.current
    val filtersState by viewModel.filters.collectAsState()

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
            SearchBar(
                query = filtersState.query,
                onChangeQuery = viewModel::updateQuery,
                onStartSearch = {
                    analytics.logEvent(
                        AnalyticsEvent(
                            AnalyticsAction.SEARCH,
                            ContentType.IMAGE_BUTTON
                        )
                    )
                    viewModel.startSearch()
                }
            )
            Tabs(
                category = filtersState.category,
                onChangeCategory = viewModel::updateCategory
            )
            SearchContent(
                filters = filtersState,
                photos = viewModel.photos,
                collections = viewModel.collections,
                users = viewModel.users,
                onPhotoClick = onPhotoClick,
                onUserClick = onUserClick,
                onCollectionClick = onCollectionClick,
            )
        }

        BottomSheetFilter(
            modalBottomSheetState = bottomSheetState,
            filterHandler = viewModel.filterHandler,
            onApplyClick = viewModel::updateFilterStateAndStartSearch
        )
    }
}

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onChangeQuery: (String) -> Unit,
    onStartSearch: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onChangeQuery,
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
                modifier = Modifier.clickable { onStartSearch() },
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
            onStartSearch()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
internal fun Tabs(
    modifier: Modifier = Modifier,
    category: SearchCategory,
    onChangeCategory: (SearchCategory) -> Unit
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
            selectedTabIndex = category.ordinal,
            indicator = { TabIndicator(tabPosition = it[category.ordinal]) },
            divider = {},
            edgePadding = 0.dp
        ) {
            SearchCategory.values().forEach { searchCategory ->
                Tab(
                    text = searchCategory.name,
                    selected = category == searchCategory,
                    onClick = { onChangeCategory(searchCategory) }
                )
            }
        }
    }
}

@Composable
internal fun SearchContent(
    filters: SearchScreenFiltersState,
    photos: Flow<PagingData<Photo>>,
    collections: Flow<PagingData<Collection>>,
    users: Flow<PagingData<User>>,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onCollectionClick: (String) -> Unit,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (filters.category) {
            SearchCategory.PHOTOS ->
                PagingPullRefreshVerticalStaggeredGrid(
                    items = photos,
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
                    onEmpty = {
                        EmptyPlaceHolder()
                    }
                )

            SearchCategory.COLLECTIONS ->
                PagingPullRefreshVerticalColumn(
                    items = collections,
                    itemCard = { collection ->
                        CollectionCard(
                            collection = collection,
                            onClick = { onCollectionClick(it.id) }
                        )
                    },
                    space = 20.dp,
                    onEmpty = {
                        EmptyPlaceHolder()
                    }
                )

            SearchCategory.USERS ->
                PagingPullRefreshVerticalColumn(
                    items = users,
                    itemCard = {
                        UserPreviewCard(
                            user = it,
                            onUserClick = onUserClick,
                            onPhotoClick = onPhotoClick
                        )
                    },
                    space = 20.dp,
                    onEmpty = {
                        EmptyPlaceHolder()
                    }
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetFilter(
    modalBottomSheetState: SheetState,
    filterHandler: FilterHandler,
    onApplyClick: (FilterUIState) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (modalBottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = modalBottomSheetState,
        ) {
            Filter(
                modifier = Modifier.padding(bottom = 40.dp),
                filterHandler = filterHandler,
                onCancelClick = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onApplyClick = { state ->
                    scope.launch {
                        onApplyClick(state)
                        modalBottomSheetState.hide()
                    }
                }
            )
        }
    }
}

@Composable
internal fun EmptyPlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.lets_find_something_incredible),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

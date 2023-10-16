package com.currencycheck.presentation.ui.main.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.currencycheck.presentation.R
import com.currencycheck.presentation.navigation.FilterOptions
import com.currencycheck.presentation.navigation.MainScreen
import com.currencycheck.presentation.ui.connection.compose.ConnectivityStatus
import com.currencycheck.presentation.ui.favorites.compose.RateItem
import com.currencycheck.presentation.ui.favorites.compose.showFavoritesView
import com.currencycheck.presentation.ui.helper.CurrencyHelper.getAvailableCurrencies
import com.currencycheck.presentation.ui.helper.CurrencyHelper.getErrorMessage
import com.currencycheck.presentation.ui.main.CurrencyState
import com.currencycheck.presentation.ui.main.MainCurrencyViewModel
import com.currencycheck.presentation.ui.main.MainCurrencyViewModel.Companion.TO_FILTER_KEY
import com.currencycheck.presentation.ui.main.data.MainViewModelData
import com.currencycheck.presentation.ui.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MainScreen(
    navController: NavController, savedEntries: SavedStateHandle,
    currencyScreenViewModel: MainCurrencyViewModel = hiltViewModel()
) {
    val refreshState = remember { mutableStateOf(false) }

    val currencyScreenState by currencyScreenViewModel.state.collectAsState()

    val toRefreshData = savedEntries.get<Boolean>(TO_FILTER_KEY) ?: false
    var selectedCurrency by remember { mutableStateOf(currencyScreenViewModel.getDefaultCurrency()) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshState.value),
        onRefresh = {
            refreshState.value = true
            currencyScreenViewModel.loadCurrencyInfo(selectedCurrency)
        },
    ) {
        if(!currencyScreenState.isLoading) {
            refreshState.value = false
        }

        Surface(color = Color.White) {
            MainScreenView(
                currentCurrency = selectedCurrency,
                currencyScreenState = currencyScreenState,
                onDropDownClick = { selectedNewCurrency ->
                    selectedCurrency = selectedNewCurrency
                    currencyScreenViewModel.loadCurrencyInfo(selectedCurrency)
                },
                onNavigateToFilter = { navController.navigate(FilterOptions.route) },
                onNavigateToCurrencies = { navController.navigate(MainScreen.route) },
                onFavoriteClicked = { isActive, from, to ->
                    currencyScreenViewModel.updateFavoriteStatus(isActive, from, to)
                }
            )
        }
    }

    if (toRefreshData) {
        currencyScreenViewModel.loadCurrencyInfo(selectedCurrency)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(
    currentCurrency: String,
    currencyScreenState: CurrencyState,
    onDropDownClick: (selectedCurrency: String) -> Unit,
    onNavigateToFilter: () -> Unit,
    onNavigateToCurrencies: () -> Unit,
    onFavoriteClicked: (isActive: Boolean, currencyFrom: String, currencyTo: String) -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    Scaffold(topBar = {
        MainTopBar(
            currencyScreenState.isLoading || !currencyScreenState.error.isNullOrEmpty(),
            if (selectedItem == 0) stringResource(id = R.string.currencies) else stringResource(
                id = R.string.favorites
            ), currentCurrency, selectedItem, onDropDownClick = { selectedCurrency ->
                if (!currencyScreenState.isLoading) {
                    onDropDownClick(selectedCurrency)
                }
            }, onNavigateToFilter
        )
    }, bottomBar = {
        bottomBar(currencyScreenState, onNavigateToCurrencies, onNavigateToFavorites = { index ->
            selectedItem = index
        })
    }, content = { paddings ->
        body(
            currencyScreenState,
            paddings,
            selectedItem,
            onFavoriteClicked = { isActive, from, to ->
                if (!currencyScreenState.isLoading) {
                    onFavoriteClicked(isActive, from, to)
                }
            })
    })
}

@Composable
fun bottomBar(
    currencyScreenState: CurrencyState,
    onNavigateToCurrencies: () -> Unit,
    onNavigateToFavorites: (index: Int) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(94.dp), tonalElevation = 0.dp
    ) {
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf(
            stringResource(id = R.string.currencies), stringResource(id = R.string.favorites)
        )
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .height(1.dp)
                    .background(color = OutlineColor)
            ) {}

            NavigationBar(
                modifier = Modifier.fillMaxSize(), tonalElevation = 0.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = TextDefaultColor,
                        indicatorColor = LightPrimaryColor,
                        unselectedIconColor = SecondaryColor,
                        unselectedTextColor = SecondaryColor
                    ),
                        icon = {
                            Icon(
                                painter = painterResource(id = if (index == 0) R.drawable.ic_currencies else R.drawable.ic_favorites_inactive),
                                contentDescription = item
                            )
                        },
                        label = { Text(item, style = MaterialTheme.typography.titleMedium) },
                        selected = selectedItem == index,
                        onClick = {
                            if (!currencyScreenState.isLoading) {
                                selectedItem = index
                                if (selectedItem == 0) {
                                    onNavigateToCurrencies()
                                } else {
                                    onNavigateToFavorites(selectedItem)
                                }
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun body(
    currencyScreenState: CurrencyState,
    paddings: PaddingValues,
    selectedPosition: Int,
    onFavoriteClicked: (isActive: Boolean, currencyFrom: String, currencyTo: String) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                if (currencyScreenState.isLoading) {
                    if (!currencyScreenState.error.isNullOrEmpty()) {
                        val errorMessageToShow = getErrorMessage(
                            context, currencyScreenState.errorCode,
                            currencyScreenState.error
                        ).toString()
                        showErrorView(paddings, errorMessageToShow)
                    } else {
                        showLoadingView(paddings)
                    }
                } else {
                    if (selectedPosition == 0) {
                        showCurrencyViews(
                            currencyScreenState.currencyModelData,
                            onFavoriteClicked = { isActive, from, to ->
                                onFavoriteClicked(isActive, from, to)
                            })
                    } else {
                        showFavoritesView(paddings)
                    }
                }
            }
        }
    }
}

@Composable
fun showErrorView(paddings: PaddingValues, errorMessage: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddings)
    ) {
        Text(
            text = errorMessage, style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun showLoadingView(paddings: PaddingValues) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddings)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun showCurrencyViews(
    mainCurrenciesData: MainViewModelData?,
    onFavoriteClicked: (isActive: Boolean, currencyFrom: String, currencyTo: String) -> Unit
) {
    mainCurrenciesData?.rates?.forEach { rateItem ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
        ) {
            RateItem(
                rateItem.currency,
                rateItem.rate,
                rateItem.isFavorite,
                onFavoriteClicked = {
                    onFavoriteClicked(it, mainCurrenciesData.baseCurrency, rateItem.currency)
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    isLoadingOrError: Boolean,
    title: String,
    defaultCurrency: String,
    selectedItem: Int,
    onDropDownClick: (selectedCurrency: String) -> Unit,
    onNavigateToFilter: () -> Unit
) {
    Column(Modifier.background(HeaderColor)) {
        ConnectivityStatus()
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(
                title, color = TextDefaultColor, style = MaterialTheme.typography.headlineLarge
            )
        })

        if (selectedItem == 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .background(Color.Transparent)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .weight(1F)
                ) {
                    CurrencySelectorView(
                        isLoadingOrError,
                        defaultCurrency,
                        onDropDownClick = { selectedCurrency ->
                            onDropDownClick(selectedCurrency)
                        })
                }
                Row(
                ) {
                    FilterView(isLoadingOrError, onNavigateToFilter)
                }
            }
        }
    }
}

@Composable
fun FilterView(isLoadingOrError: Boolean, onNavigateToFilter: () -> Unit) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp, color = SecondaryColor, shape = RoundedCornerShape(size = 8.dp)
            )
            .width(48.dp)
            .height(48.dp)
            .clickable {
                if (!isLoadingOrError) {
                    onNavigateToFilter()
                }
            }
            .background(color = OnPrimaryColor, shape = RoundedCornerShape(size = 8.dp)),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                .clickable {
                    if (!isLoadingOrError) {
                        onNavigateToFilter()
                    }
                },
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        ) {
            Image(
                modifier = Modifier
                    .padding(1.dp)
                    .width(24.dp)
                    .height(24.dp),
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )
        }
    }
}

@Composable
fun CurrencySelectorView(
    isLoadingOrError: Boolean,
    defaultCurrency: String,
    onDropDownClick: (selectedCurrency: String) -> Unit
) {
    // State variables
    var currencySelectedName: String by remember { mutableStateOf(defaultCurrency) }

    var expanded by remember { mutableStateOf(false) }

    collapsedDropDownMenu(currencySelectedName, onDropDownClick = {
        expanded = !expanded
    })

    if (expanded) {
        PopupWindowDialog(isLoadingOrError,
            currencySelectedName,
            onItemClicked = { currencyName ->
                currencySelectedName = currencyName
                onDropDownClick(currencySelectedName)
            },
            onHideMenu = {
                expanded = false
            }
        )
    }
}

@Composable
fun PopupWindowDialog(
    isLoadingOrError: Boolean,
    defaultCurrency: String,
    onItemClicked: (currencyName: String) -> Unit,
    onHideMenu: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    val dialogWidth = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .onGloballyPositioned {
                dialogWidth.value = it.parentLayoutCoordinates?.size?.width ?: 0
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            val size = with(LocalDensity.current) { 0.toDp() }
            val size1 = with(LocalDensity.current) { dialogWidth.value.toDp() }
            val popupWidth = size1 - size

            if (openDialog.value && !isLoadingOrError) {
                Popup(
                    offset = IntOffset(
                        0.dp.value.toInt(),
                        with(LocalDensity.current) { -24.dp.toPx() }.toInt()
                    ),
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(dismissOnClickOutside = true)
                ) {
                    showOrHideExpandedMenu(popupWidth, defaultCurrency,
                        onItemClicked = { currencyName ->
                            openDialog.value = !openDialog.value
                            onItemClicked(currencyName)
                        },
                        onHideMenu = {
                            onHideMenu()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun showOrHideExpandedMenu(
    popupWidth: Dp,
    defaultCurrency: String,
    onItemClicked: (currencyName: String) -> Unit,
    onHideMenu: () -> Unit
) {
    val availableCurrencies = getAvailableCurrencies()

    Box(
        Modifier
            .shadow(
                elevation = 6.dp,
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .shadow(
                elevation = 2.dp,
                spotColor = Color(0x4D000000),
                ambientColor = Color(0x4D000000)
            )
            .border(width = 1.dp, color = SecondaryColor, shape = RoundedCornerShape(size = 8.dp))
            .width(popupWidth)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            selectedCurrencyMainView(showingPopup = true, defaultCurrency, onDropDownClick = {
                onHideMenu()
            })

            availableCurrencies.map { item ->
                Column(
                    modifier = Modifier
                        .width(272.dp)
                        .height(56.dp)
                        .background(color = if (item == defaultCurrency) LightPrimaryColor else DefaultBackgroundColor)
                        .clickable {
                            onHideMenu()
                            onItemClicked(item)
                        },
                    verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier
                            .width(272.dp)
                            .height(48.dp)
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                0.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = item,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight(500),
                                    color = TextDefaultColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun collapsedDropDownMenu(
    currencySelectedName: String,
    onDropDownClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp, color = SecondaryColor, shape = RoundedCornerShape(size = 8.dp)
            )
            .height(48.dp)
            .background(
                color = DefaultBackgroundColor, shape = RoundedCornerShape(size = 8.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.weight(1f)) {
            selectedCurrencyMainView(showingPopup = false, currencySelectedName, onDropDownClick = {
                onDropDownClick()
            })
        }
    }
}

@Composable
fun selectedCurrencyMainView(
    showingPopup: Boolean,
    currencySelectedName: String,
    onDropDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .clickable {
                onDropDownClick()
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = currencySelectedName,
            style = MaterialTheme.typography.titleSmall
        )

        Image(
            modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp),
            painter = painterResource(id = if (showingPopup) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
            contentDescription = stringResource(id = R.string.currency_list_shower),
            contentScale = ContentScale.None
        )
    }
}

@Preview
@Composable
fun RateItemPreview() {
    RateItem("EUR", 3.5555, false, onFavoriteClicked = {})
}
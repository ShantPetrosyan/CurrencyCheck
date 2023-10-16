package com.currencycheck.presentation.ui.filter.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.currencycheck.presentation.R
import com.currencycheck.presentation.ui.filter.FilterState
import com.currencycheck.presentation.ui.filter.FilterViewModel
import com.currencycheck.presentation.ui.main.MainCurrencyViewModel.Companion.TO_FILTER_KEY
import com.currencycheck.presentation.ui.theme.*

@Composable
fun FilterScreen(
    navController: NavController, filterViewModel: FilterViewModel = hiltViewModel()
) {
    val filterScreenState by filterViewModel.state.collectAsState()

    if (filterScreenState.filterApplied) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(TO_FILTER_KEY, true)
        navController.navigateUp()
    }

    Surface(color = Color.White) {
        FilterScreenView(filterScreenState = filterScreenState,
            navController = navController,
            onFilterSelected = { selectedFilterPosition ->
                filterViewModel.updateSelectedFilter(selectedFilterPosition)
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenView(
    filterScreenState: FilterState,
    navController: NavController,
    onFilterSelected: (selectedFilterPosition: Int) -> Unit
) {
    var selectedFilterItemPosition by remember { mutableIntStateOf(0) }

    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HeaderColor,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(
                stringResource(id = R.string.filters),
                color = TextDefaultColor,
                style = MaterialTheme.typography.headlineLarge
            )
        }, navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = PrimaryColor
                    )
                }
            } else {
                null
            }
        })
    }, bottomBar = {
        FilterBottomBar(onApplyClicked = {
            onFilterSelected(selectedFilterItemPosition)
        })
    }, content = { paddings ->
        FilterBody(paddings, filterScreenState, onFilterSelectionChanged = { position ->
            selectedFilterItemPosition = position
        })
    })
}

@Composable
fun FilterBottomBar(onApplyClicked: () -> Unit) {
    BottomAppBar(
        modifier = Modifier.height(80.dp), tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(color = PrimaryColor, shape = RoundedCornerShape(size = 100.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(start = 24.dp, top = 10.dp, end = 24.dp, bottom = 10.dp)
                    .clickable {
                        onApplyClicked()
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.apply), style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(500),
                        color = OnPrimaryColor,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.1.sp
                    )
                )
            }
        }
    }
}

@Composable
fun FilterBody(
    paddings: PaddingValues,
    filterScreenState: FilterState,
    onFilterSelectionChanged: (selectedFilterPosition: Int) -> Unit
) {
    var selectedFilterItemPosition by remember {
        var selectedIndex = 0
        filterScreenState.filterViewModelDataList.forEachIndexed { index, item ->
            if (item.isSelected) {
                selectedIndex = index
            }
        }
        mutableIntStateOf(selectedIndex)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sort_by), style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(700),
                            color = TextSecondaryColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                filterScreenState.filterViewModelDataList.forEachIndexed { index, rateItem ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                                selectedFilterItemPosition = index
                                onFilterSelectionChanged(selectedFilterItemPosition)
                            }
                            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    selectedFilterItemPosition = index
                                    onFilterSelectionChanged(selectedFilterItemPosition)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = rateItem.filterName, style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight(500),
                                    color = TextDefaultColor
                                )
                            )

                            Row(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                                    .clickable {
                                        selectedFilterItemPosition = index
                                        onFilterSelectionChanged(selectedFilterItemPosition)
                                    },
                                horizontalArrangement = Arrangement.spacedBy(
                                    0.dp, Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(40.dp)
                                        .padding(
                                            start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp
                                        )
                                        .clickable {
                                            selectedFilterItemPosition = index
                                            onFilterSelectionChanged(selectedFilterItemPosition)
                                        },
                                    horizontalArrangement = Arrangement.spacedBy(
                                        0.dp, Alignment.CenterHorizontally
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = if (index == selectedFilterItemPosition) R.drawable.ic_radio_button_selected
                                            else R.drawable.ic_radio_button_unselected
                                        ),
                                        contentDescription = stringResource(id = R.string.filter_selector),
                                        contentScale = ContentScale.None
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
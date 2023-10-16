package com.currencycheck.presentation.ui.favorites.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.currencycheck.presentation.R
import com.currencycheck.presentation.ui.favorites.FavoritesViewModel
import com.currencycheck.presentation.ui.main.compose.showLoadingView
import com.currencycheck.presentation.ui.theme.CardColor
import com.currencycheck.presentation.ui.theme.TextDefaultColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun showFavoritesView(
    paddings: PaddingValues,
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    onFavoriteClicked: (isActive: Boolean, currencyFrom: String, currencyTo: String) -> Unit
) {
    val favoritesScreenState by favoritesViewModel.state.collectAsState()

    if (favoritesScreenState.favoritesViewModelDataList.isEmpty()) {
        if (favoritesScreenState.isLoading) {
            showLoadingView(paddings)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_favorites),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = TextDefaultColor
                )
            }
        }
    } else {
        favoritesScreenState.favoritesViewModelDataList.forEach { favoriteData ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
            ) {
                RateItem(
                    "${favoriteData.currencyFrom}/${favoriteData.currencyTo}",
                    favoriteData.currencyAmount,
                    true,
                    favoritesScreenState.isLoading,
                    onFavoriteClicked = {
                        onFavoriteClicked(it, favoriteData.currencyFrom, favoriteData.currencyTo)
                    }
                )
            }
        }
    }
}

@Composable
fun RateItem(
    currencyName: String,
    exchangeValue: Double,
    isFavorite: Boolean,
    isFavoriteLoading: Boolean = false,
    onFavoriteClicked: (isActive: Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(48.dp)
            .background(color = CardColor, shape = RoundedCornerShape(size = 12.dp))
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = currencyName, style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.weight(1F),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = exchangeValue.toString(), style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = TextDefaultColor,
                        fontWeight = FontWeight(600),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (isFavoriteLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                            .background(Color.White)
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Image(
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.favorites_selected
                            else R.drawable.favorites_unselected
                        ),
                        contentDescription = stringResource(id = R.string.add_to_favorites),
                        contentScale = ContentScale.None,
                        modifier = Modifier.clickable {
                            onFavoriteClicked(!isFavorite)
                        }
                    )
                }
            }
        }
    }
}
package com.example.travappupd.presentation.view


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentPasteSearch
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.travappupd.R
import com.example.travappupd.data.entities.Trip
import com.example.travappupd.presentation.viewmodel.PreviewTripsViewModel
import com.example.travappupd.presentation.viewmodel.TripViewModel
import com.example.travappupd.presentation.viewmodel.TripsViewModel
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    onNavigateToDetails: (tripId: Long, tripName: String) -> Unit,
    onNavigateBack: () -> Unit,
    type: String,
    viewModel: TripsViewModel = hiltViewModel()
) {
    val curTrips by viewModel.curTrips.collectAsStateWithLifecycle()
    val pastTrips by viewModel.pastTrips.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var isSearchActive by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val trips = if (type == "plan") curTrips else pastTrips
    Log.e("Тип путешествия", "Переданный тип $type")

    fun deleteWithSnackbar(trip: Trip) {
        viewModel.deleteWithUndo(trip)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Поездка «${trip.title}» удалена")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text("Поиск по названию") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isSearchActive) {
                            viewModel.updateSearchQuery("")
                        }
                        isSearchActive = !isSearchActive
                    }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Outlined.Close else Icons.Outlined.Search,
                            contentDescription = if (isSearchActive) "Закрыть поиск" else "Поиск"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F8FC)),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                if (!isSearchActive) {
                    if (type == "plan") {
                        HeaderSection(
                            stringResource(R.string.plans),
                            stringResource(R.string.plans_subtitle),
                            0
                        )

                    } else {
                        HeaderSection(
                            stringResource(R.string.archive),
                            stringResource(R.string.archive_subtitle),
                            0
                        )
                    }
                }
            }
            if (trips.isEmpty() && searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "Ничего не найдено",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ExtendedTheme.colors.textColor,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }

            for (trip in trips) {
                item {
                    TripCard(
                        photo = painterResource(R.drawable.trip),
                        title = trip.title,
                        date = "${trip.startDate} - ${trip.endDate}",
                        places = "place",
                        onClick = { onNavigateToDetails(trip.tripId, trip.title) },
                        onDelete = { }
                    )
                }
            }
        }
    }
}


@Composable
fun TripCard(
    modifier: Modifier = Modifier,
    photo: Painter,
    title: String,
    date: String,
    places: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart
        }
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd ||
            dismissState.currentValue == SwipeToDismissBoxValue.EndToStart
        )
        {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(15.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                    Alignment.CenterStart
                } else {
                    Alignment.CenterEnd
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFFFF)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = modifier
                .height(100.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                Image(
                    painter = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clip(RoundedCornerShape(10))
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier
                        .weight(2f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ExtendedTheme.colors.titleColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.height(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            tint = ExtendedTheme.colors.textColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.height(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            tint = ExtendedTheme.colors.textColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = places,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }

                Icon(
                    imageVector =Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = ExtendedTheme.colors.textColor
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    title: String,
    text: String,
    extend: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height((80+extend).dp)
    ) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = ExtendedTheme.colors.titleColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ExtendedTheme.colors.textColor
        )

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewTripsScreen() {
    TravelAppTheme() {
        val mockViewModel = remember { PreviewTripsViewModel() }
        TripsScreen(
            onNavigateToDetails = { tripId, tripName -> },
            onNavigateBack = {},
            type = "preview",
            viewModel = mockViewModel
        )
    }
}


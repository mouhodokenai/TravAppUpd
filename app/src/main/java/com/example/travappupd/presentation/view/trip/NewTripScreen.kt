package com.example.travappupd.presentation.view.trip

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.presentation.view.HeaderSection
import com.example.travappupd.presentation.view.dop.DatePickerModal
import com.example.travappupd.presentation.viewmodel.PreviewTripViewModel
import com.example.travappupd.presentation.viewmodel.TripViewModel
import com.example.travappupd.ui.theme.TravelAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.example.travappupd.ui.theme.ExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTrips: (String) -> Unit,
    onNavigateToBudget: (Long) -> Unit,
    onNavigateToHotel: (Long) -> Unit,
    onNavigateToNote: (Long) -> Unit,
    onNavigateToPackingList: (Long) -> Unit,
    onNavigateToRoute: (Long) -> Unit,
    onNavigateToTicket: (Long) -> Unit,
    viewModel: TripViewModel
) {
    val context = LocalContext.current

    val tripName by viewModel.tripName.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    val startDateString = formatLocalDate(startDate)
    val endDateString = formatLocalDate(endDate)

    val tripId = viewModel.tripId

    var isExpanded by remember { mutableStateOf(false) }

    val budgetItems by viewModel.budgetRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val hotelItems by viewModel.hotelRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val noteItems by viewModel.noteRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val packingItems by viewModel.packingListRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val routeItems by viewModel.routeRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val ticketItems by viewModel.ticketRepository.getItemsByTripId(viewModel.tripId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val categories = listOf(
        budgetItems, hotelItems, noteItems, packingItems, routeItems, ticketItems
    ).count { it.isNotEmpty() }

    Scaffold(
        bottomBar = {
            BottomCreateButton(
                false,
                onSave = {
                    if (ChronoUnit.DAYS.between(startDate, endDate) > 0) {
                        viewModel.saveTrip()
                        onNavigateToTrips("plan")
                    }
                    else Toast.makeText(context, "Ошибка при выборе даты", Toast.LENGTH_SHORT).show()
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = null
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
                .background(Color(0xFFF7F8FC)), //???
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                HeaderSection(
                    "Новая поездка",
                    "Спланируйте ваше идеальное путешествие",
                    0
                )
            }

            item {
                TripPreviewCard(
                    tripName = tripName,
                    startDate = startDate,
                    endDate = endDate,
                    isExpanded = isExpanded,
                    categories = categories,
                    onToggleExpanded = { isExpanded = !isExpanded }
                )
            }

            item {
                TripInfoSection(
                    tripName = tripName,
                    startDate = startDate,
                    endDate = endDate,
                    onTripNameChange = { viewModel.updateTripName(it) },
                    onStartDateChange = { date -> viewModel.updateStartDate(date) },
                    onEndDateChange = { date -> viewModel.updateEndDate(date) },
                    isExpanded = isExpanded
                )
            }

            item {
                Text(
                    text = "Категории поездки",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            item {

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CategoryCard(
                        icon = painterResource(R.drawable.location2),
                        title = "Маршрут",
                        subtitle = " ",
                        color =  ExtendedTheme.colors.routeColor2,
                        onAdd = { onNavigateToRoute(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.wallet),
                        title = "Бюджет",
                        subtitle = " ",
                        color = ExtendedTheme.colors.budgetColor2,
                        onAdd = { onNavigateToBudget(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.ticket),
                        title = "Билеты",
                        subtitle = " ",
                        color =  ExtendedTheme.colors.ticketColor2, //???
                        onAdd = { onNavigateToTicket(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.hotel),
                        title = "Отели",
                        subtitle = " ",
                        color = ExtendedTheme.colors.hotelColor2,
                        onAdd = { onNavigateToHotel(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.luggage2),
                        title = "Багаж",
                        subtitle = " ",
                        color = ExtendedTheme.colors.baggageColor2,
                        onAdd = { onNavigateToPackingList(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.note),
                        title = "Заметки",
                        subtitle = " ",
                        color = ExtendedTheme.colors.noteColor2,
                        onAdd = { onNavigateToNote(tripId) }
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Create Trip Screen"
)
@Composable
fun CreateTripScreenPreview() {
    TravelAppTheme() {
        val mockViewModel = remember { PreviewTripViewModel(
        ) }
        NewTripScreen(
            onNavigateBack = {},
            viewModel = mockViewModel,
            onNavigateToTrips = {},
            onNavigateToBudget = {},
            onNavigateToHotel = {},
            onNavigateToNote = {},
            onNavigateToPackingList = {},
            onNavigateToRoute = {},
            onNavigateToTicket = {}
        )
    }
}
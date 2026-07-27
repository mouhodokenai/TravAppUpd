package com.example.travappupd.presentation.view

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
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.presentation.viewmodel.PreviewTripViewModel
import com.example.travappupd.presentation.viewmodel.TripViewModel
import com.example.travappupd.ui.theme.TravelAppTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
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
    viewModel: TripViewModel = hiltViewModel()
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

        ,
        bottomBar = {
            BottomCreateButton(
                onSave = {
                    if (ChronoUnit.DAYS.between(startDate, endDate) > 0) {
                        viewModel.saveTrip()
                        onNavigateToTrips("plan")
                    }
                    else Toast.makeText(context, "Ошибка при выборе даты", Toast.LENGTH_SHORT).show()
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
                    "Спланируйте ваше идеальное путешествие"
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
                    startDate = startDateString,
                    endDate = endDateString,
                    onTripNameChange = { viewModel.updateTripName(it) },
                    onStartDateChange = { dateString ->
                        viewModel.updateStartDate(dateString)
                    },

                    onEndDateChange = { dateString ->
                        viewModel.updateEndDate(dateString)
                    },
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

                    CategoryCard( //???
                        icon = painterResource(R.drawable.location2),
                        title = "Маршрут",
                        subtitle = " ",
                        color = Color(0xFF62B44B),
                        onAdd = { onNavigateToRoute(tripId) }
                    )

                    CategoryCard(
                        icon = painterResource(R.drawable.luggage2),
                        title = "Багаж",
                        subtitle = " ",
                        color = ExtendedTheme.colors.baggageColor2,
                        onAdd = { onNavigateToPackingList(tripId) }
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
                        color = Color(0xFF8D63FF), //???
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

private fun parseToLocalDate(dateString: String): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
        LocalDate.parse(dateString, formatter)
    } catch (e: Exception) {
        null
    }
}

private fun formatLocalDate(date: LocalDate?): String {
    return date?.let {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
        it.format(formatter)
    } ?: "выбрать"
}

@Composable
private fun TripPreviewCard(
    tripName: String,
    startDate: LocalDate?,
    endDate: LocalDate?,
    isExpanded: Boolean,
    categories: Int,
    onToggleExpanded: () -> Unit
) {

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "\uD83C\uDF10",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = tripName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text =
                            if (startDate != null && endDate != null)
                                if (ChronoUnit.DAYS.between(startDate, endDate) > 0)
                                "${ChronoUnit.DAYS.between(startDate, endDate)} дней"
                                else "ошибка при выборе даты"
                        else "0 дней",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { 0.17f * categories },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "$categories из 6 разделов",
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onToggleExpanded() }
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
private fun TripInfoSection(
    tripName: String,
    startDate: String,
    endDate: String,
    onTripNameChange: (String) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    isExpanded: Boolean
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        //visible = true
        visible = isExpanded
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Название поездки",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        BasicTextField(
                            value = tripName,
                            onValueChange = onTripNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ),
                            cursorBrush = SolidColor(
                                MaterialTheme.colorScheme.primary
                            ),
                            decorationBox = { innerTextField ->

                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    if (tripName.isBlank()) {
                                        Text(
                                            text = "Введите название",
                                            color = ExtendedTheme.colors.textColor,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }


            OutlinedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    EditableDateBlock(
                        title = "Дата начала",
                        date = startDate,
                        onDateClick = onStartDateChange,
                        placeholder = "выбрать"
                    )

                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    EditableDateBlock(
                        title = "Дата окончания",
                        date = endDate.toString(),
                        onDateClick = onEndDateChange,
                        placeholder = "выбрать"
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableDateBlock(
    title: String,
    date: String,
    onDateClick: (LocalDate) -> Unit,
    placeholder: String = "выбрать"
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = date.ifEmpty { placeholder },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (date == placeholder) ExtendedTheme.colors.textColor else Color(0xFF46465E) ///???
            )
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                onDateClick(selectedDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = " "
        )
    }
}




@Composable
private fun CategoryCard(
    icon: Painter,
    title: String,
    subtitle: String,
    itemCount: Int = 0,
    color: Color,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { onAdd() }
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = icon, contentDescription = null, tint = color)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (itemCount > 0) "Добавлено: $itemCount" else subtitle,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun BottomCreateButton(
    onSave: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        tonalElevation = 8.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Button(
                onClick = {
                    onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExtendedTheme.colors.newTripColor,
                    contentColor = ExtendedTheme.colors.textColor
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Создать путешествие"
                )
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
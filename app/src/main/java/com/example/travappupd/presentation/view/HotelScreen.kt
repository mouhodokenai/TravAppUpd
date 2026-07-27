package com.example.travappupd.presentation.view

import android.annotation.SuppressLint
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.presentation.viewmodel.CurrencyTotal
import com.example.travappupd.presentation.viewmodel.HotelViewModel
import com.example.travappupd.presentation.viewmodel.PreviewHotelViewModel
import com.example.travappupd.presentation.viewmodel.nightsBetween
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import kotlin.collections.forEach


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: HotelViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val hotelItems by viewModel.hotelItems.collectAsStateWithLifecycle()
    val totalNights by viewModel.totalNights.collectAsStateWithLifecycle()

    var editingHotel by remember { mutableStateOf<Hotel?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    fun deleteWithSnackbar(hotel: Hotel) {
        viewModel.deleteWithUndo(hotel)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Отель «${hotel.name}» удалён")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.hotell),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    editingHotel = null
                    showSheet = true
                },
                    containerColor = ExtendedTheme.colors.hotelColor
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        action = {
                            IconButton(onClick = { data.performAction() }) {
                                Text("Отменить", color = MaterialTheme.colorScheme.inversePrimary)
                            }
                        }
                    ) { Text(data.visuals.message) }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {

                Row(
                    modifier = Modifier.padding(
                        PaddingValues(
                            horizontal = 20.dp,
                            vertical = 8.dp
                        )
                    )
                ) {
                    HeaderSection(
                        "Отели",
                        "Проживание во время поездки"
                    )
                }

                HotelSummaryCard(
                    hotelCount = hotelItems.size,
                    totalNights = totalNights,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                if (hotelItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет отелей. Добавьте первый, нажав на кнопку «+».",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ExtendedTheme.colors.textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(hotelItems, key = { it.hotelId }) { hotel ->
                            HotelCard(
                                hotel = hotel,
                                onClick = {
                                    editingHotel = hotel
                                    showSheet = true
                                },
                                onDelete = { deleteWithSnackbar(hotel) }
                            )
                        }
                    }
                }
            }
        }

        if (showSheet) {
            HotelFormBottomSheet(
                sheetState = sheetState,
                tripId = tripId,
                editingHotel = editingHotel,
                onDismiss = { showSheet = false },
                onSave = { hotel ->
                    if (nightsBetween(hotel.checkInDate, hotel.checkOutDate) < 0)
                        Toast.makeText(context, "Ошибка при выборе даты", Toast.LENGTH_SHORT).show()
                    else {
                        if (editingHotel != null) {
                            viewModel.updateItem(hotel)
                        } else {
                            viewModel.addItem(hotel)
                        }
                        showSheet = false
                    }

                },
                onDelete = { hotel ->
                    showSheet = false
                    deleteWithSnackbar(hotel)
                }
            )
        }
    }
}

@Composable
private fun HotelSummaryCard(
    hotelCount: Int,
    totalNights: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ExtendedTheme.colors.summaryCardColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Отелей",
                    style = MaterialTheme.typography.bodySmall,
                    color = ExtendedTheme.colors.textColor
                )
                Text(
                    text = hotelCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Ночей всего",
                    style = MaterialTheme.typography.bodySmall,
                    color = ExtendedTheme.colors.textColor
                )
                Text(
                    text = totalNights.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelCard(
    hotel: Hotel,
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
        ) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                    Alignment.CenterStart
                } else {
                    Alignment.CenterEnd
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEDEAFB), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Hotel,
                        contentDescription = null,
                        tint = ExtendedTheme.colors.hotelColor2
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hotel.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = hotel.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = ExtendedTheme.colors.textColor,
                        maxLines = 1
                    )
                    Text(
                        text = "${hotel.checkInDate} → ${hotel.checkOutDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExtendedTheme.colors.textColor
                    )
                }

                Text(
                    text = "${nightsBetween(hotel.checkInDate, hotel.checkOutDate)} ноч.",
                    style = MaterialTheme.typography.labelMedium,
                    color = ExtendedTheme.colors.textColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelFormBottomSheet(
    sheetState: SheetState,
    tripId: Long,
    editingHotel: Hotel?,
    onDismiss: () -> Unit,
    onSave: (Hotel) -> Unit,
    onDelete: ((Hotel) -> Unit)? = null
) {
    val isEditing = editingHotel != null

    var name by remember { mutableStateOf(editingHotel?.name ?: "") }
    var address by remember { mutableStateOf(editingHotel?.address ?: "") }
    var checkInDate by remember { mutableStateOf(editingHotel?.checkInDate ?: "") }
    var checkOutDate by remember { mutableStateOf(editingHotel?.checkOutDate ?: "") }
    var checkInTime by remember { mutableStateOf(editingHotel?.checkInTime ?: "14:00") }
    var checkOutTime by remember { mutableStateOf(editingHotel?.checkOutTime ?: "12:00") }

    var showCheckInDatePicker by remember { mutableStateOf(false) }
    var showCheckOutDatePicker by remember { mutableStateOf(false) }
    var showCheckInTimePicker by remember { mutableStateOf(false) }
    var showCheckOutTimePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (isEditing) "Изменить отель" else "Новый отель",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название отеля") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Адрес") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text("Заезд", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = checkInDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    placeholder = { Text("ГГГГ-ММ-ДД") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showCheckInDatePicker = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = checkInTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Время") },
                    modifier = Modifier
                        .width(110.dp)
                        .clickable { showCheckInTimePicker = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("Выезд", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = checkOutDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    placeholder = { Text("ГГГГ-ММ-ДД") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showCheckOutDatePicker = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = checkOutTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Время") },
                    modifier = Modifier
                        .width(110.dp)
                        .clickable { showCheckOutTimePicker = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isEditing && onDelete != null) {
                    IconButton(onClick = { editingHotel.let(onDelete) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                    }
                }

                Button(
                    onClick = {
                            val hotel = (editingHotel ?: Hotel(
                                tripId = tripId,
                                name = "",
                                address = "",
                                checkInDate = "",
                                checkOutDate = "",
                                checkInTime = "",
                                checkOutTime = ""
                            )).copy(
                                name = name,
                                address = address,
                                checkInDate = checkInDate,
                                checkOutDate = checkOutDate,
                                checkInTime = checkInTime,
                                checkOutTime = checkOutTime
                            )
                            onSave(hotel)

                    },
                    enabled = name.isNotBlank() && checkInDate.isNotBlank() && checkOutDate.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }

    if (showCheckInDatePicker) {
        DatePickerModal(
            initialDate = checkInDate,
            onDateSelected = { checkInDate = it.toString(); showCheckInDatePicker = false },
            onDismiss = { showCheckInDatePicker = false }
        )
    }
    if (showCheckOutDatePicker) {
        DatePickerModal(
            initialDate = checkOutDate,
            onDateSelected = { checkOutDate = it.toString(); showCheckOutDatePicker = false },
            onDismiss = { showCheckOutDatePicker = false }
        )
    }
    if (showCheckInTimePicker) {
        TimePickerModal(
            initialTime = checkInTime,
            onTimeSelected = { checkInTime = it.toString(); showCheckInTimePicker = false },
            onDismiss = { showCheckInTimePicker = false }
        )
    }
    if (showCheckOutTimePicker) {
        TimePickerModal(
            initialTime = checkOutTime,
            onTimeSelected = { checkOutTime = it.toString(); showCheckOutTimePicker = false },
            onDismiss = { showCheckOutTimePicker = false }
        )
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HotelScreenPreview() {
    TravelAppTheme {
        HotelScreen(
            onNavigateBack = {},
            tripId = 1,
            viewModel = PreviewHotelViewModel()
        )
    }
}
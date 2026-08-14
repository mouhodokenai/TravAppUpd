package com.example.travappupd.presentation.view

import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalTime
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.presentation.view.dop.DatePickerModal
import com.example.travappupd.presentation.view.dop.TicketTypes
import com.example.travappupd.presentation.view.dop.TimePickerModal
import com.example.travappupd.presentation.viewmodel.PreviewTicketViewModel
import com.example.travappupd.presentation.viewmodel.TicketViewModel
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: TicketViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val ticketItems by viewModel.ticketItems.collectAsStateWithLifecycle()

    var editingTicket by remember { mutableStateOf<Ticket?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun deleteWithSnackbar(ticket: Ticket) {
        val typeLabel = TicketTypes.infoFor(ticket.transportType).label
        viewModel.deleteWithUndo(ticket)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Билет «$typeLabel» удалён")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.tickets),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        editingTicket = null
                        showSheet = true
                    },
                    containerColor = ExtendedTheme.colors.ticketColor
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
                        "Билеты",
                        "Ваши билеты на все виды транспорта",
                        0
                    )
                }
                if (ticketItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет билетов. Добавьте первый, нажав на кнопку «+».",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ExtendedTheme.colors.textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(ticketItems, key = { it.ticketId }) { ticket ->
                            TicketCard(
                                ticket = ticket,
                                onClick = {
                                    editingTicket = ticket
                                    showSheet = true
                                },
                                onDelete = { deleteWithSnackbar(ticket) }
                            )
                        }
                    }
                }
            }
        }

        if (showSheet) {
            TicketFormBottomSheet(
                sheetState = sheetState,
                tripId = tripId,
                editingTicket = editingTicket,
                existingCustomTypes = ticketItems.map { it.transportType },
                onDismiss = { showSheet = false },
                onSave = { ticket ->
                    if (editingTicket != null) {
                        viewModel.updateItem(ticket)
                    } else {
                        viewModel.addItem(ticket)
                    }
                    showSheet = false
                },
                onDelete = { ticket ->
                    showSheet = false
                    deleteWithSnackbar(ticket)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val typeInfo = TicketTypes.infoFor(ticket.transportType)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart
        }
    )

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

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
                    contentDescription = "Удалить",
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
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(typeInfo.containerColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeInfo.icon,
                            contentDescription = null,
                            tint = typeInfo.contentColor
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${ticket.departureCity} → ${ticket.arrivalCity}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = typeInfo.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = ExtendedTheme.colors.textColor
                        )
                    }

                    if (ticket.ticketNumber.isNotBlank()) {
                        Text(
                            text = ticket.ticketNumber,
                            style = MaterialTheme.typography.bodyLarge,
                            color = ExtendedTheme.colors.textColor
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = ticket.departureDate.format(dateFormatter) +
                                    (ticket.departureTime.let {
                                        " ·  ${it?.format(timeFormatter)}"
                                    }.orEmpty()),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ExtendedTheme.colors.textColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = ticket.arrivalDate.format(dateFormatter) +
                                    (ticket.arrivalTime?.let {
                                        " · ${it.format(timeFormatter)}"
                                    }.orEmpty()),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ExtendedTheme.colors.textColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormBottomSheet(
    sheetState: SheetState,
    tripId: Long,
    editingTicket: Ticket?,
    existingCustomTypes: List<String>,
    onDismiss: () -> Unit,
    onSave: (Ticket) -> Unit,
    onDelete: ((Ticket) -> Unit)? = null
) {
    val isEditing = editingTicket != null
    val selectableTypes = remember(existingCustomTypes) {
        TicketTypes.selectableTypes(existingCustomTypes)
    }

    var selectedTypeKey by remember {
        mutableStateOf(editingTicket?.transportType ?: selectableTypes.first().key)
    }
    var showCustomTypeInput by remember { mutableStateOf(false) }
    var customTypeText by remember { mutableStateOf("") }

    var departureCity by remember { mutableStateOf(editingTicket?.departureCity ?: "") }
    var arrivalCity by remember { mutableStateOf(editingTicket?.arrivalCity ?: "") }
    var departureDate by remember { mutableStateOf(editingTicket?.departureDate) }
    var arrivalDate by remember { mutableStateOf(editingTicket?.arrivalDate) }
    var departureTime by remember { mutableStateOf(editingTicket?.departureTime) }
    var arrivalTime by remember { mutableStateOf(editingTicket?.arrivalTime) }
    var ticketNumber by remember { mutableStateOf(editingTicket?.ticketNumber ?: "") }

    var showDepartureDatePicker by remember { mutableStateOf(false) }
    var showArrivalDatePicker by remember { mutableStateOf(false) }
    var showDepartureTimePicker by remember { mutableStateOf(false) }
    var showArrivalTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    val isFormValid = departureCity.isNotBlank() && arrivalCity.isNotBlank() &&
            departureDate != null && arrivalDate != null

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (isEditing) "Изменить билет" else "Новый билет",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))
            Text("Тип транспорта", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectableTypes, key = { it.key }) { type ->
                    FilterChip(
                        selected = selectedTypeKey == type.key && !showCustomTypeInput,
                        onClick = {
                            selectedTypeKey = type.key
                            showCustomTypeInput = false
                        },
                        leadingIcon = { Icon(type.icon, contentDescription = null) },
                        label = { Text(type.label) }
                    )
                }
                item {
                    AssistChip(
                        onClick = { showCustomTypeInput = true },
                        leadingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                        label = { Text("Свой вариант") }
                    )
                }
            }

            if (showCustomTypeInput) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = customTypeText,
                    onValueChange = { customTypeText = it },
                    label = { Text("Тип транспорта") },
                    placeholder = { Text("Например, «Такси»") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = departureCity,
                    onValueChange = { departureCity = it },
                    label = { Text("Откуда") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = arrivalCity,
                    onValueChange = { arrivalCity = it },
                    label = { Text("Куда") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("Отправление", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = departureDate?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Дата *") },
                    placeholder = { Text("Выберите дату") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDepartureDatePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = departureTime?.format(timeFormatter) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Время") },
                    placeholder = { Text("—") },
                    modifier = Modifier
                        .width(110.dp)
                        .clickable { showDepartureTimePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("Прибытие", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = arrivalDate?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Дата *") },
                    placeholder = { Text("Выберите дату") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showArrivalDatePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = arrivalTime?.format(timeFormatter) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Время") },
                    placeholder = { Text("—") },
                    modifier = Modifier
                        .width(110.dp)
                        .clickable { showArrivalTimePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = ticketNumber,
                onValueChange = { ticketNumber = it },
                label = { Text("Номер билета") },
                placeholder = { Text("Необязательно") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isEditing && onDelete != null) {
                    IconButton(onClick = { editingTicket.let(onDelete) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Удалить билет")
                    }
                }

                Button(
                    onClick = {
                        val finalTypeKey = if (showCustomTypeInput && customTypeText.isNotBlank()) {
                            customTypeText.trim()
                        } else {
                            selectedTypeKey
                        }
                        val ticket = editingTicket?.copy(
                            transportType = finalTypeKey,
                            departureCity = departureCity,
                            arrivalCity = arrivalCity,
                            departureTime = departureTime,
                            arrivalTime = arrivalTime,
                            departureDate = departureDate!!,
                            arrivalDate = arrivalDate!!,
                            ticketNumber = ticketNumber
                        )
                            ?: Ticket(
                                tripId = tripId,
                                transportType = finalTypeKey,
                                departureCity = departureCity,
                                arrivalCity = arrivalCity,
                                departureTime = departureTime,
                                arrivalTime = arrivalTime,
                                departureDate = departureDate!!,
                                arrivalDate = arrivalDate!!,
                                ticketNumber = ticketNumber
                            )

                        onSave(ticket)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }

    if (showDepartureDatePicker) {
        DatePickerModal(
            initialDate = departureDate,
            onDateSelected = { departureDate = it; showDepartureDatePicker = false },
            onDismiss = { showDepartureDatePicker = false }
        )
    }
    if (showArrivalDatePicker) {
        DatePickerModal(
            initialDate = arrivalDate,
            onDateSelected = { arrivalDate = it; showArrivalDatePicker = false },
            onDismiss = { showArrivalDatePicker = false }
        )
    }
    if (showDepartureTimePicker) {
        TimePickerModal(
            initialTime = departureTime ?: LocalTime.of(12, 0),
            onTimeSelected = { departureTime = it; showDepartureTimePicker = false },
            onDismiss = { showDepartureTimePicker = false }
        )
    }
    if (showArrivalTimePicker) {
        TimePickerModal(
            initialTime = arrivalTime ?: LocalTime.of(12, 0),
            onTimeSelected = { arrivalTime = it; showArrivalTimePicker = false },
            onDismiss = { showArrivalTimePicker = false }
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TicketScreenPreview() {
    TravelAppTheme() {
        TicketScreen(
            onNavigateBack = {},
            tripId = 1,
            viewModel = PreviewTicketViewModel()
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview()
@Composable
fun TicketCardPreview() {
    TravelAppTheme() {
        TicketCard(
            ticket = Ticket(
                ticketId = 1,
                tripId = 1,
                transportType = "flight",
                departureCity = "Москва",
                arrivalCity = "Рим",
                departureTime = LocalTime.parse("14:30"),
                arrivalTime = null,
                departureDate = LocalDate.parse("2026-08-01"),
                arrivalDate = LocalDate.parse("2026-08-01"),
                ticketNumber = "SU-2145"
            ),
            onClick = {},
            onDelete = {}
        )
    }
}



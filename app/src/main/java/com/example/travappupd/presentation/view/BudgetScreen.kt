package com.example.travappupd.presentation.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.presentation.viewmodel.PreviewTripViewModel
import com.example.travappupd.ui.theme.TravelAppTheme
import java.time.temporal.ChronoUnit
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room3.Delete
import com.example.travappupd.R
import com.example.travappupd.presentation.viewmodel.BudgetViewModel
import com.example.travappupd.presentation.viewmodel.CurrencyTotal
import com.example.travappupd.presentation.viewmodel.PreviewBudgetViewModel
import com.example.travelapp.budget.BudgetCategories
import com.example.travelapp.budget.SUPPORTED_CURRENCIES
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel()
) {

    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val budgetItems by viewModel.budgetItems.collectAsStateWithLifecycle()
    val totalsByCurrency by viewModel.totalsByCurrency.collectAsStateWithLifecycle()
    val categoryCount by viewModel.categoryCount.collectAsStateWithLifecycle()

    var editingBudget by remember { mutableStateOf<Budget?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun deleteWithSnackbar(budget: Budget) {
        val categoryLabel = BudgetCategories.infoFor(budget.category).label
        viewModel.deleteWithUndo(budget)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Расход «$categoryLabel» удалён")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.money),
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
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = null)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    editingBudget = null
                    showSheet = true
                },
                    containerColor = Color(0xFFDEEDE7)
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

                Row(modifier = Modifier.padding(PaddingValues(
                    horizontal = 20.dp,
                    vertical = 8.dp
                ))) {
                    HeaderSection(
                        "Бюджет",
                        "Планирование расходов поездки"
                    )
                }

                BudgetSummaryCard(
                    totalsByCurrency = totalsByCurrency,
                    categoryCount = categoryCount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                if (budgetItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет расходов. Добавьте первый, нажав на кнопку «+».",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        items(budgetItems, key = { it.budgetId }) { budget ->
                            BudgetExpenseCard(
                                budget = budget,
                                onClick = {
                                    editingBudget = budget
                                    showSheet = true
                                },
                                onDelete = { deleteWithSnackbar(budget) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        BudgetFormBottomSheet(
            sheetState = sheetState,
            tripId = tripId,
            editingBudget = editingBudget,
            existingCustomCategories = budgetItems.map { it.category },
            onDismiss = { showSheet = false },
            onSave = { budget ->
                if (editingBudget != null) {
                    viewModel.updateItem(budget)
                } else {
                    viewModel.addItem(budget)
                }
                showSheet = false
            },
            onDelete = { budget ->
                showSheet = false
                deleteWithSnackbar(budget)
            }
        )
    }
}

@Composable
private fun BudgetSummaryCard(
    totalsByCurrency: List<CurrencyTotal>,
    categoryCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Всего запланировано",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (totalsByCurrency.isEmpty()) {
                        Text("—", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
                    } else {
                        totalsByCurrency.forEach { total ->
                            Text(
                                text = formatTotal(total.amount) + " " + total.currency,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Категорий",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = categoryCount.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun formatTotal(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    formatter.maximumFractionDigits = if (amount == amount.toLong().toDouble()) 0 else 2
    return formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetExpenseCard(
    budget: Budget,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryInfo = BudgetCategories.infoFor(budget.category)
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
                        .background(categoryInfo.containerColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryInfo.icon,
                        contentDescription = null,
                        tint = categoryInfo.contentColor
                    )
                }

                Text(
                    text = categoryInfo.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatAmount(budget.amount) + " " + budget.currency,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun formatAmount(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    formatter.maximumFractionDigits = if (amount == amount.toLong().toDouble()) 0 else 2
    return formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BudgetFormBottomSheet(
    sheetState: SheetState,
    tripId: Long,
    editingBudget: Budget?,
    existingCustomCategories: List<String>,
    onDismiss: () -> Unit,
    onSave: (Budget) -> Unit,
    onDelete: ((Budget) -> Unit)? = null
) {
    val isEditing = editingBudget != null
    val selectableCategories = remember(existingCustomCategories) {
        BudgetCategories.selectableCategories(existingCustomCategories)
    }

    var selectedCategoryKey by remember {
        mutableStateOf(editingBudget?.category ?: selectableCategories.first().key)
    }
    var amountText by remember { mutableStateOf(editingBudget?.amount?.let { formatAmountInput(it) } ?: "") }
    var currency by remember { mutableStateOf(editingBudget?.currency ?: SUPPORTED_CURRENCIES.first()) }
    var showCustomCategoryInput by remember { mutableStateOf(false) }
    var customCategoryText by remember { mutableStateOf("") }
    var currencyMenuExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (isEditing) "Изменить расход" else "Новый расход",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))
            Text("Категория", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectableCategories, key = { it.key }) { cat ->
                    FilterChip(
                        selected = selectedCategoryKey == cat.key && !showCustomCategoryInput,
                        onClick = {
                            selectedCategoryKey = cat.key
                            showCustomCategoryInput = false
                        },
                        leadingIcon = { Icon(cat.icon, contentDescription = null) },
                        label = { Text(cat.label) }
                    )
                }
                item {
                    AssistChip(
                        onClick = { showCustomCategoryInput = true },
                        leadingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                        label = { Text("Своя категория") }
                    )
                }
            }

            if (showCustomCategoryInput) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = customCategoryText,
                    onValueChange = { customCategoryText = it },
                    label = { Text("Название категории") },
                    placeholder = { Text("Например, «Подарки»") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { value -> amountText = value.filter { it.isDigit() || it == '.' || it == ',' } },
                    label = { Text("Сумма") },
                    placeholder = { Text("0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(12.dp))

                ExposedDropdownMenuBox(
                    expanded = currencyMenuExpanded,
                    onExpandedChange = { currencyMenuExpanded = it },
                    modifier = Modifier.width(110.dp)
                ) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Валюта") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyMenuExpanded) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = currencyMenuExpanded,
                        onDismissRequest = { currencyMenuExpanded = false }
                    ) {
                        SUPPORTED_CURRENCIES.forEach { code ->
                            DropdownMenuItem(
                                text = { Text(code) },
                                onClick = {
                                    currency = code
                                    currencyMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isEditing && onDelete != null) {
                    IconButton(onClick = { editingBudget?.let(onDelete) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                    }
                }

                Button(
                    onClick = {
                        val amount = amountText.replace(',', '.').toDoubleOrNull() ?: 0.0
                        val finalCategoryKey = if (showCustomCategoryInput && customCategoryText.isNotBlank()) {
                            customCategoryText.trim()
                        } else {
                            selectedCategoryKey
                        }
                        val budget = (editingBudget ?: Budget(tripId = tripId, category = "", amount = 0.0, currency = "")).copy(
                            category = finalCategoryKey,
                            amount = amount,
                            currency = currency
                        )
                        onSave(budget)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}

private fun ColumnScope.item(function: Any) {}

private fun formatAmountInput(amount: Double): String =
    if (amount == amount.toLong().toDouble()) amount.toLong().toString() else amount.toString()

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun BudgetScreenPreview() {
    TravelAppTheme() {
        BudgetScreen(
            onNavigateBack = {},
            tripId = 1,
            viewModel = PreviewBudgetViewModel()
        )
    }
}
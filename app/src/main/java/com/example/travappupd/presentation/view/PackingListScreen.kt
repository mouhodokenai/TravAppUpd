package com.example.travappupd.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.presentation.viewmodel.PackingListViewModel
import com.example.travappupd.presentation.viewmodel.PreviewPackingListViewModel
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackingListScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: PackingListViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val baggageItems by viewModel.baggageItems.collectAsStateWithLifecycle()
    val itemsCount by viewModel.baggageCount.collectAsStateWithLifecycle()
    val itemsPackedCount by viewModel.baggagePackedCount.collectAsStateWithLifecycle()

    var editingItem by remember { mutableStateOf<PackingList?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun deleteWithSnackbar(item: PackingList) {
        viewModel.deleteWithUndo(item)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Вещь «$item.name» удалена")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.baggage),
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
                        editingItem = null
                        showSheet = true
                    },
                    containerColor = ExtendedTheme.colors.baggageColor
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
                        "Багаж",
                        "Добавьте вещи, необходимые в путешествии",
                        0
                    )
                }
                BaggageSummaryCard(
                    itemsCount = itemsCount,
                    itemsPacked = itemsPackedCount
                )
                if (baggageItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет вещей. Добавьте первую, нажав на кнопку «+».",
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
                        items(baggageItems, key = { it.itemId }) { item ->
                            BaggageCard(
                                item = item,
                                onClick = {
                                    editingItem = item
                                    showSheet = true
                                },
                                onDelete = { deleteWithSnackbar(item) },
                                onTogglePacked = { checked ->
                                    viewModel.updateItem(item.copy(isPacked = checked))
                                }
                            )
                        }
                    }
                }

                if (showSheet) {
                    BaggageBottomSheet(
                        sheetState = sheetState,
                        tripId = tripId,
                        editingItem = editingItem,
                        onDismiss = { showSheet = false },
                        onSave = { item ->
                            if (editingItem != null) {
                                viewModel.updateItem(item)
                            } else {
                                viewModel.addItem(item)
                            }
                            showSheet = false
                        },
                        onDelete = { item ->
                            showSheet = false
                            deleteWithSnackbar(item)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaggageBottomSheet(
    sheetState: SheetState,
    tripId: Long,
    editingItem: PackingList?,
    onDismiss: () -> Unit,
    onSave: (PackingList) -> Unit,
    onDelete: ((PackingList) -> Unit)? = null
) {
    val isEditing = editingItem != null

    var name by remember { mutableStateOf(editingItem?.name ?: "") }


    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (isEditing) "Изменить вещь" else "Новая вещь",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                placeholder = { Text("Например, «Документы»") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isEditing && onDelete != null) {
                    IconButton(onClick = { editingItem.let(onDelete) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                    }
                }
                Button(
                    onClick = {
                        val item = (editingItem ?: PackingList(
                            tripId = tripId,
                            name = ""
                        )).copy(
                            name = name
                        )
                        onSave(item)
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaggageCard(
    item: PackingList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onTogglePacked: (Boolean) -> Unit
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
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconToggleButton(
                    checked = item.isPacked,
                    onCheckedChange = { checked -> onTogglePacked(checked) }
                ) {
                    Icon(
                        imageVector = if (item.isPacked) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = if (item.isPacked) "Собрано" else "Не собрано",
                        tint = if (item.isPacked) ExtendedTheme.colors.baggageColor2 else ExtendedTheme.colors.textColor,
                        modifier = Modifier.scale(1.3f)
                    )
                }

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = ExtendedTheme.colors.titleColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}



@Composable
private fun BaggageSummaryCard(
    itemsCount: Int,
    itemsPacked: Int,
    modifier: Modifier = Modifier
) {
    val progressCount = 1 / itemsCount.toFloat()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(ExtendedTheme.colors.summaryCardColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Всего вещей",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExtendedTheme.colors.textColor
                    )

                    Text(itemsCount.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Собрано",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExtendedTheme.colors.textColor
                    )

                    Text(itemsPacked.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Осталось собрать",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExtendedTheme.colors.textColor
                    )
                    Text(
                        text = (itemsCount-itemsPacked).toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { progressCount * itemsPacked },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = ExtendedTheme.colors.baggageColor2
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PackingListScreenPreview() {
    TravelAppTheme() {
        PackingListScreen(
            tripId = 1,
            onNavigateBack = { },
            viewModel = PreviewPackingListViewModel()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaggageSummaryCardPreview() {
    TravelAppTheme() {
        BaggageSummaryCard(
            3,
            2
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaggageCardPreview() {
    TravelAppTheme() {
        BaggageCard(
            PackingList(1, 1, "Вещь"),
            onClick = { },
            onDelete = { },
            onTogglePacked = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaggageBottomSheetPreview() {
    TravelAppTheme() {
        BaggageBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            tripId = 1,
            editingItem = null,
            onDismiss = {},
            onSave = { } ,
            onDelete = { }
        )
    }
}
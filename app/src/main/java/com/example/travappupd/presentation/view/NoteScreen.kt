package com.example.travappupd.presentation.view

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.data.entities.Note
import com.example.travappupd.presentation.viewmodel.NoteViewModel
import com.example.travappupd.presentation.viewmodel.PreviewNoteViewModel
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val noteItems by viewModel.noteItems.collectAsStateWithLifecycle()

    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    fun deleteWithSnackbar(note: Note) {
        viewModel.deleteWithUndo(note)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Отель «${note.title}» удалён")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.notes),
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                editingNote = null
                showSheet = true
            },
                containerColor = ExtendedTheme.colors.noteColor
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        },


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
                    "Заметки",
                    "Добавьте необходимые записи о путешествии"
                )
            }

            if (noteItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пока нет записей. Добавьте первую, нажав на кнопку «+».",
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
                    items(noteItems, key = { it.noteId }) { note ->
                        NoteCard(
                            note = note,
                            onClick = {
                                editingNote = note
                                showSheet = true
                            },
                            onDelete = { deleteWithSnackbar(note) }
                        )
                    }
                }
            }
        }
    }
    }

    if (showSheet) {
        NoteFormBottomSheet(
            sheetState = sheetState,
            tripId = tripId,
            editingNote = editingNote,
            onDismiss = { showSheet = false },
            onSave = { note ->
                if (editingNote != null) {
                    viewModel.updateItem(note)
                } else {
                    viewModel.addItem(note)
                }
                showSheet = false

            },
            onDelete = { note ->
                showSheet = false
                deleteWithSnackbar(note)
            },
            existingCustomTitles = noteItems.map { it.title },
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val titleInfo = NoteTitles.infoFor(note.title)
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
                    imageVector = titleInfo.icon,
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
                        imageVector = titleInfo.icon,
                        contentDescription = null,
                        tint = ExtendedTheme.colors.noteColor2
                    )
                }

                Text(
                    text = titleInfo.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormBottomSheet(
    sheetState: SheetState,
    tripId: Long,
    editingNote: Note?,
    existingCustomTitles: List<String>,
    onDismiss: () -> Unit,
    onSave: (Note) -> Unit,
    onDelete: ((Note) -> Unit)? = null
) {
    val isEditing = editingNote != null
    val selectableTitles = remember(existingCustomTitles) {
        NoteTitles.selectableTitles(existingCustomTitles)
    }

    var selectedTitleKey by remember {
        mutableStateOf(editingNote?.title ?: selectableTitles.first().key)
    }
    var text by remember { mutableStateOf(editingNote?.content ?: "") }
    var showCustomTitleInput by remember { mutableStateOf(false) }
    var customTitleText by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (isEditing) "Изменить запись" else "Новая запись",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))
            Text("Заголовок", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectableTitles, key = { it.key }) { titleInfo ->
                    FilterChip(
                        selected = selectedTitleKey == titleInfo.key && !showCustomTitleInput,
                        onClick = {
                            selectedTitleKey = titleInfo.key
                            showCustomTitleInput = false
                        },
                        leadingIcon = { Icon(titleInfo.icon, contentDescription = null) },
                        label = { Text(titleInfo.label) }
                    )
                }
                item {
                    AssistChip(
                        onClick = { showCustomTitleInput = true },
                        leadingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                        label = { Text("Свой заголовок") }
                    )
                }
            }

            if (showCustomTitleInput) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = customTitleText,
                    onValueChange = { customTitleText = it },
                    label = { Text("Название заголовка") },
                    placeholder = { Text("Например, «Планы»") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Введите текст...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 300.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isEditing && onDelete != null) {
                    IconButton(onClick = { editingNote.let(onDelete) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                    }
                }

                Button(
                    onClick = {
                        val finalTitleKey = if (showCustomTitleInput && customTitleText.isNotBlank()) {
                            customTitleText.trim()
                        } else {
                            selectedTitleKey
                        }
                        val note = (editingNote ?: Note(
                            tripId = tripId,
                            title = "",
                            content = ""
                        )).copy(
                            title = finalTitleKey,
                            content = text
                        )
                        onSave(note)
                    },
                    enabled = text.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NoteScreenPreview() {
    TravelAppTheme {
        NoteScreen(
            onNavigateBack = {},
            tripId = 1,
            viewModel = PreviewNoteViewModel()
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun NoteFormBottomSheetPreview() {
//    TravelAppTheme() {
//        NoteFormBottomSheet(
//            sheetState = rememberModalBottomSheetState(),
//            tripId = 1,
//            editingNote = null,
//            onDismiss = {  },
//            onSave = {  },
//            onDelete = {  }
//        )
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun NoteCardPreview() {
//    TravelAppTheme() {
//        NoteCard(
//            note = null,
//            onClick = { },
//            onDelete = { }
//        )
//    }
//}
package com.example.travappupd.presentation.view

import androidx.compose.runtime.Composable
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import androidx.compose.foundation.ExperimentalFoundationApi
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.draw.clipToBounds
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travappupd.R
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.repositories.GeocodingResult
import com.example.travappupd.presentation.viewmodel.PreviewRouteViewModel
//import com.example.travappupd.presentation.viewmodel.PreviewRouteViewModel
import com.example.travappupd.presentation.viewmodel.RouteViewModel
import com.example.travappupd.presentation.viewmodel.TicketViewModel
import com.example.travappupd.ui.theme.ExtendedTheme
import com.example.travappupd.ui.theme.TravelAppTheme
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import androidx.core.graphics.toColorInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: RouteViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) { viewModel.selectTrip(tripId) }

    val routeItems by viewModel.routeItems.collectAsStateWithLifecycle()

//    var editingPlace by remember { mutableStateOf<Route?>(null) }
//    var showSheet by remember { mutableStateOf(false) }

    var viewingRoute by remember { mutableStateOf<Route?>(null) }
    var showAddSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val addSheetState = rememberModalBottomSheetState()
    val detailSheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val lineColor = ExtendedTheme.colors.routeColor2.toArgb()

    fun deleteWithSnackbar(route: Route) {
        viewModel.deleteWithUndo(route)
        scope.launch {
            val result = snackbarHostState.showSnackbar(message = "Место «${route.name}» удалено")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearPendingDelete()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.routemap),
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
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedTab == 0) {
                    FloatingActionButton(
                        onClick = {
//                            editingPlace = null
                            showAddSheet = true
                        },
                        containerColor = ExtendedTheme.colors.routeColor
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                    }
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
            Column(
                modifier = Modifier
                .padding(padding)
                .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.padding(
                        PaddingValues(
                            horizontal = 20.dp,
                            vertical = 8.dp
                        )
                    )
                ) {
                    HeaderSection(
                        "Маршрут",
                        "Добавьте города и места, которые планируете посетить",
                        10
                    )
                }

                TabRow(
                    modifier = Modifier
                        .alpha(0.7f)
                        .zIndex(1f),
                    selectedTabIndex = selectedTab,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = ExtendedTheme.colors.routeColor2
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Список") },
                        selectedContentColor = ExtendedTheme.colors.routeColor2,
                        unselectedContentColor = ExtendedTheme.colors.textColor
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Карта") },
                        selectedContentColor = ExtendedTheme.colors.routeColor2,
                        unselectedContentColor = ExtendedTheme.colors.textColor
                    )
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clipToBounds()
                ) {
                    when (selectedTab) {
                        0 -> RouteListContent(
                            routeItems = routeItems,
                            onClick = { route -> viewingRoute = route },
                            onDelete = { route -> deleteWithSnackbar(route) },
                            onReorder = { newOrder -> viewModel.reorderItems(newOrder) }
                        )

                        1 -> if (routeItems.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Добавьте хотя бы одну точку, чтобы увидеть карту",
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
                            RouteMapView(routePoints = routeItems)
                        }
                    }
                }
            }
        }

        if (showAddSheet) {
            RouteAddBottomSheet(
                sheetState = addSheetState,
                searchQuery = searchQuery,
                searchResults = searchResults,
                isSearching = isSearching,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onDismiss = {
                    showAddSheet = false
                    viewModel.clearSearch()
                },
                onSelectResult = { result ->
                    viewModel.addRoutePoint(result.name, result.address, result.latitude, result.longitude)
                    showAddSheet = false
                    viewModel.clearSearch()
                }
            )
        }

        viewingRoute?.let { route ->
            RouteDetailBottomSheet(
                sheetState = detailSheetState,
                route = route,
                onDismiss = { viewingRoute = null },
                onDelete = {
                    viewingRoute = null
                    deleteWithSnackbar(route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteAddBottomSheet(
    sheetState: SheetState,
    searchQuery: String,
    searchResults: List<GeocodingResult>,
    isSearching: Boolean,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSelectResult: (GeocodingResult) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
                .heightIn(max = 500.dp)
        ) {
            Text("Новая точка", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                label = { Text("Поиск места") },
                placeholder = { Text("Например, «Колизей, Рим»") },
                singleLine = true,
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            if (searchResults.isEmpty() && searchQuery.isNotBlank() && !isSearching) {
                Text(
                    text = "Ничего не найдено",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(     searchResults) { result ->
                        Card(
                            onClick = { onSelectResult(result) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(result.name, fontWeight = FontWeight.Medium)
                                    Text(
                                        text = result.address,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailBottomSheet(
    sheetState: SheetState,
    route: Route,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp, bottom = 12.dp)
                .navigationBarsPadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(route.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            }

            route.address?.let { address ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Координаты: ${route.latitude}, ${route.longitude}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                Spacer(Modifier.width(8.dp))
                Text("Удалить точку", color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RouteListContent(
    routeItems: List<Route>,
    onClick: (Route) -> Unit,
    onDelete: (Route) -> Unit,
    onReorder: (List<Route>) -> Unit
) {
    if (routeItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Пока нет точек маршрута. Добавьте первую, нажав на кнопку «+».",
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
        return
    }

    var localItems by remember(routeItems) { mutableStateOf(routeItems) }

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        localItems = localItems.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(localItems, key = { _, route -> route.routeId }) { index, route ->
            ReorderableItem(reorderableState, key = route.routeId) { isDragging ->
                RouteCard(
                    route = route,
                    position = index + 1,
                    isDragging = isDragging,
                    onClick = { onClick(route) },
                    onDelete = { onDelete(route) },
                    dragHandleModifier = Modifier.draggableHandle(
                        onDragStopped = { onReorder(localItems) }
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(
    route: Route,
    position: Int,
    isDragging: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    dragHandleModifier: Modifier
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
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(if (isDragging) 8.dp else 0.dp, RoundedCornerShape(16.dp)),
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
                        .size(32.dp)
                        .background(ExtendedTheme.colors.routeColor2, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = position.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = route.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ExtendedTheme.colors.titleColor,
                        fontWeight = FontWeight.Medium
                    )
                    route.address?.let { address ->
                        Text(
                            text = address,
                            style = MaterialTheme.typography.bodySmall,
                            color = ExtendedTheme.colors.textColor,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Outlined.DragHandle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = dragHandleModifier
                )
            }
        }
    }
}

@Composable
fun RouteMapView(
    routePoints: List<Route>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cartoLightTileSource = XYTileSource(
        "CartoLight",
        0, 20, 256, ".png",
        arrayOf(
            "https://a.basemaps.cartocdn.com/light_all/",
            "https://b.basemaps.cartocdn.com/light_all/",
            "https://c.basemaps.cartocdn.com/light_all/"
        )
    )
    val lineColor = ExtendedTheme.colors.routeColor2.toArgb()

    val mapView = remember {
        MapView(context).apply {
            setTileSource(cartoLightTileSource)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.overlays.clear()

            if (routePoints.isNotEmpty()) {
                val sortedPoints = routePoints.sortedBy { it.orderIndex }
                val geoPoints = sortedPoints.map { GeoPoint(it.latitude, it.longitude) }

                val markerDrawable = ContextCompat.getDrawable(context, R.drawable.pin)

                sortedPoints.forEachIndexed { index, route ->
                    val marker = Marker(view).apply {
                        position = GeoPoint(route.latitude, route.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "${index + 1}. ${route.name}"
                        snippet = route.address
                        icon = createResizedMarkerIcon(context, R.drawable.pin, sizeDp = 32)
                    }
                    view.overlays.add(marker)
                }

                if (geoPoints.size > 1) {
                    val polyline = Polyline().apply {
                        setPoints(geoPoints)
                        outlinePaint.color = lineColor
                        outlinePaint.strokeWidth = 6f
                    }
                    view.overlays.add(polyline)
                }

                val boundingBox = if (geoPoints.size == 1) {
                    val point = geoPoints.first()
                    BoundingBox(point.latitude + 0.01, point.longitude + 0.01, point.latitude - 0.01, point.longitude - 0.01)
                } else {
                    BoundingBox.fromGeoPoints(geoPoints)
                }
                view.post { view.zoomToBoundingBox(boundingBox, true, 100) }
            }

            view.invalidate()
        }
    )
}

private fun createNumberedMarkerBitmap(
    context: Context,
    number: Int,
    backgroundColor: Int
): BitmapDrawable {
    val size = 80
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4f, circlePaint)

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4f, borderPaint)

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    val textY = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
    canvas.drawText(number.toString(), size / 2f, textY, textPaint)

    return BitmapDrawable(context.resources, bitmap)
}

private fun createResizedMarkerIcon(
    context: Context,
    drawableRes: Int,
    sizeDp: Int
): BitmapDrawable? {
    val vectorDrawable = ContextCompat.getDrawable(context, drawableRes) ?: return null
    val sizePx = (sizeDp * context.resources.displayMetrics.density).toInt()

    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)

    return BitmapDrawable(context.resources, bitmap)
}

@Composable
private fun RouteMapPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Карта появится здесь на следующем шаге",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RouteScreenPreview() {
    TravelAppTheme() {
        RouteScreen(
            onNavigateBack = {},
            tripId = 1,
            viewModel = PreviewRouteViewModel()
        )
    }
}
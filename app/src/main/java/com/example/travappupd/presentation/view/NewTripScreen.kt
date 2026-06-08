package com.example.travappupd.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travappupd.presentation.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    onNavigateBack: () -> Unit,
    viewModel: TripViewModel = viewModel()
) {

    val tripName by viewModel.tripName.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

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
        /*
        ,
        bottomBar = {
            BottomCreateButton()
        }

         */
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F8FC)),
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
                TripPreviewCard()
            }

            item {
                TripInfoSection()
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
                        icon = Icons.Outlined.Clear,
                        title = "Багаж",
                        subtitle = "3 вещи добавлено",
                        color = Color(0xFF5B8DEF)
                    )

                    CategoryCard(
                        icon = Icons.Outlined.Clear,
                        title = "Бюджет",
                        subtitle = "1 200 €",
                        color = Color(0xFF2DBE7F)
                    )

                    CategoryCard(
                        icon = Icons.Outlined.Clear,
                        title = "Заметки",
                        subtitle = "2 заметки",
                        color = Color(0xFFFFB648)
                    )

                    CategoryCard(
                        icon = Icons.Outlined.Clear,
                        title = "Билеты",
                        subtitle = "2 билета",
                        color = Color(0xFF8D63FF)
                    )

                    CategoryCard(
                        icon = Icons.Outlined.Clear,
                        title = "Отели",
                        subtitle = "1 бронь",
                        color = Color(0xFFFF725E)
                    )

                    CategoryCard(
                        icon = Icons.Outlined.LocationOn,
                        title = "Маршрут мест",
                        subtitle = "14 мест",
                        color = Color(0xFF62B44B)
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}


@Composable
private fun TripPreviewCard() {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
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

                    Text(
                        text = "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "12–20 мая • 8 дней",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { 0.35f },
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
                    text = "2 из 6 разделов",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun TripInfoSection( //тут остановились
    // Данные
    tripName: String,
    startDate: String,
    endDate: String,
    // Колбэки для изменений
    onTripNameChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onSave: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedCard(
            shape = RoundedCornerShape(24.dp)
        ) {

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Text(
                        text = "Название поездки",
                        color = Color.Gray
                    )

                    Text(
                        text = "Путешествие в Италию",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        OutlinedCard(
            shape = RoundedCornerShape(24.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                DateBlock(
                    title = "Дата начала",
                    date = "12 мая 2024"
                )

                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null
                )

                DateBlock(
                    title = "Дата окончания",
                    date = "20 мая 2024"
                )
            }
        }
    }
}

@Composable
private fun DateBlock(
    title: String,
    date: String
) {

    Column {

        Text(
            text = title,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = date,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CategoryCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {
            // navigate
        }
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

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
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
private fun BottomCreateButton() {

    Surface(
        tonalElevation = 8.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp)
            ) {

                Icon(
                    imageVector = Icons.Outlined.Clear,
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
    MaterialTheme {
        NewTripScreen(
            onNavigateBack = {},
            viewModel = TripViewModel()
        )
    }
}
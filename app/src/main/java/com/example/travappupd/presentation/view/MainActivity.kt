package com.example.travappupd.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travappupd.ui.theme.TravelAppTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.travappupd.R
import com.example.travappupd.navigation.TripNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelAppTheme {
                    TripNavigation()
                }
            }
        }
    }


@Composable
fun MainScreen(
    onNavigateToNewTrip: () -> Unit,
    onNavigateToTrips: (String) -> Unit
)
{
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {

                //TODO доделать панель навигации
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Outlined.Home, null)
                    },
                    label = {
                        Text("Главная")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Outlined.Clear, null)
                    },
                    label = {
                        Text("???")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Outlined.Clear, null)
                    },
                    label = {
                        Text("???")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Outlined.Clear, null)
                    },
                    label = {
                        Text("???")
                    }
                )

            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F8FC)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.hello2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFFF7F8FC)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.heading_main),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A5063)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                        }
                    }
                }
            }

            item {

                Text(
                    text = stringResource(R.string.stats),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFF4A5063)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatisticCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(R.drawable.flag),
                        number = "28",
                        title = stringResource(R.string.country_stat),
                        subtitle = stringResource(R.string.visited_stat),
                        color = Color(0xFFEF9F98),
                        containerColor = Color(0x16EF9F98)
                    )

                    StatisticCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(R.drawable.location),
                        number = "132",
                        title = stringResource(R.string.place_stat),
                        subtitle = stringResource(R.string.visited_stat),
                        color = Color(0xFF45C4A1),
                        containerColor = Color(0x1445C4A1)
                    )

                    StatisticCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(R.drawable.luggage),
                        number = "47",
                        title = stringResource(R.string.trips_stat),
                        subtitle = stringResource(R.string.total_stat),
                        color = Color(0xFF9B6BFF),
                        containerColor = Color(0x169B6BFF)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            item {

                MainActionCard(
                    title = stringResource(R.string.new_trip),
                    subtitle = stringResource(R.string.add_trip),
                    icon = Icons.Outlined.Add,
                    background = Color(0xFFA1B2F6),
                    onClick = onNavigateToNewTrip
                )

                Spacer(modifier = Modifier.height(16.dp))

                SecondaryCard(
                    title = stringResource(R.string.plans_main),
                    subtitle = stringResource(R.string.future),
                    icon = Icons.Outlined.Clear,
                    onClick = { onNavigateToTrips("plan") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SecondaryCard(
                    title = stringResource(R.string.archive_main),
                    subtitle = stringResource(R.string.past),
                    icon = Icons.Outlined.Clear,
                    onClick = { onNavigateToTrips("archive") }
                )
            }
        }
    }
}

    @Composable
    fun StatisticCard(
        modifier: Modifier = Modifier,
        icon: Painter,
        number: String,
        title: String,
        subtitle: String,
        color: Color,
        containerColor: Color
    ) {

        Card(
            modifier = modifier.height(160.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter  = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    )
                }

                Column {
                    Text(
                        text = number,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A5063)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4A5063)
                    )

                    Text(
                        text = subtitle,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    @Composable
    fun MainActionCard(
        title: String,
        subtitle: String,
        icon: ImageVector,
        background: Color,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(28.dp),
        ) {

            Row(
                modifier = Modifier
                    .background(background)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.9f)
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
fun SecondaryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xB4A0B1F4)
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFDFDFD)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = Color.White
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


@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    TravelAppTheme {
        MainScreen(
            onNavigateToNewTrip = { },
            onNavigateToTrips = { }
        )
    }
}

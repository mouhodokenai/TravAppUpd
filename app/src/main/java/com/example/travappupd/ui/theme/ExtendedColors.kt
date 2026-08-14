package com.example.travappupd.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class ExtendedColors(
    val titleColor: Color,
    val textColor: Color,
    val summaryCardColor: Color,
    val hotelColor: Color,
    val noteColor: Color,
    val baggageColor: Color,
    val budgetColor: Color,
    val hotelColor2: Color,
    val noteColor2: Color,
    val baggageColor2: Color,
    val budgetColor2: Color,
    val routeColor: Color,
    val routeColor2: Color,
    val ticketColor: Color,
    val ticketColor2: Color,
    val newTripColor: Color,
    val tripColor: Color,
    val statCountryIconColor: Color,
    val statCountryColor: Color,
    val statPlaceIconColor: Color,
    val statPlaceColor: Color,
    val statTripIconColor: Color,
    val statTripColor: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        titleColor = Color.Unspecified,
        textColor = Color.Unspecified,
        summaryCardColor = Color.Unspecified,
        hotelColor = Color.Unspecified,
        noteColor = Color.Unspecified,
        baggageColor = Color.Unspecified,
        budgetColor = Color.Unspecified,
        hotelColor2 = Color.Unspecified,
        noteColor2 = Color.Unspecified,
        baggageColor2 = Color.Unspecified,
        budgetColor2 = Color.Unspecified,
        routeColor = Color.Unspecified,
        routeColor2 = Color.Unspecified,
        ticketColor = Color.Unspecified,
        ticketColor2 = Color.Unspecified,
        newTripColor = Color.Unspecified,
        tripColor = Color.Unspecified,
        statCountryIconColor = Color.Unspecified,
        statCountryColor = Color.Unspecified,
        statPlaceIconColor = Color.Unspecified,
        statPlaceColor = Color.Unspecified,
        statTripIconColor = Color.Unspecified,
        statTripColor = Color.Unspecified
    )
}

val LightExtendedColors = ExtendedColors(
    titleColor = Color(0xFF4A5063),
    textColor = Color(0xA9464D62),
    summaryCardColor = Color(0xFFEDEDF5).copy(alpha = 0.6f),
    hotelColor = Color(0xFFF1DBF7),
    noteColor = Color(0xFFEEDCC7),
    baggageColor = Color(0xFFEFD0CE),
    budgetColor = Color(0xFFDEEDE7),
    routeColor = Color(0xFFF0D2D7),
    ticketColor = Color(0xFFC9CDFD),
    hotelColor2 = Color(0xFFF087F6),
    noteColor2 = Color(0xFFEFC99C),
    baggageColor2 = Color(0xFFF18983),
    budgetColor2 = Color(0xFF8FEFC2),
    routeColor2 = Color(0xFFF18B9B),
    ticketColor2 = Color(0xFF989FFC),
    newTripColor = Color(0xFFA1B2F6),
    tripColor = Color(0xB4A0B1F4),
    statCountryIconColor = Color(0xFFEF9F98),
    statCountryColor = Color(0x16EF9F98),
    statPlaceIconColor = Color(0xFF45C4A1),
    statPlaceColor = Color(0x1445C4A1),
    statTripIconColor = Color(0xFF9B6BFF),
    statTripColor = Color(0x169B6BFF)
)

val DarkExtendedColors = ExtendedColors(
    titleColor = Color(0xFF4A5063),
    textColor = Color(0xA9464D62),
    summaryCardColor = Color(0xFFEDEDF5).copy(alpha = 0.6f),
    hotelColor = Color(0xFFF1DBF7),
    noteColor = Color(0xFFEEDCC7),
    baggageColor = Color(0xFFEFD0CE),
    budgetColor = Color(0xFFDEEDE7),
    routeColor = Color.Unspecified,
    ticketColor = Color.Unspecified,
    hotelColor2 = Color(0xFFF1DBF7),
    noteColor2 = Color(0xFFEEDCC7),
    baggageColor2 = Color(0xFFEFD0CE),
    budgetColor2 = Color(0xFFDEEDE7),
    routeColor2 = Color.Unspecified,
    ticketColor2 = Color.Unspecified,
    newTripColor = Color(0xFFA1B2F6),
    tripColor = Color(0xB4A0B1F4),
    statCountryIconColor = Color(0xFFEF9F98),
    statCountryColor = Color(0x16EF9F98),
    statPlaceIconColor = Color(0xFF9B6BFF),
    statPlaceColor = Color(0x1445C4A1),
    statTripIconColor = Color(0xFF9B6BFF),
    statTripColor = Color(0x169B6BFF)
)
package com.example.travappupd.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Train
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TicketTypeInfo(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color,
    val isBuiltIn: Boolean = true
)

object TicketTypes {

    private val flightContainer = Color(0xFFE3F0FB)
    private val flightContent = Color(0xFF2B6CA3)

    private val trainContainer = Color(0xFFE9F3DD)
    private val trainContent = Color(0xFF5E8C2E)

    private val busContainer = Color(0xFFFCF3DC)
    private val busContent = Color(0xFFAF8324)

    private val ferryContainer = Color(0xFFE3F6EF)
    private val ferryContent = Color(0xFF2F8F70)

    private val carContainer = Color(0xFFFBEAF0)
    private val carContent = Color(0xFFB04C73)

    private val customPalette = listOf(
        flightContainer to flightContent,
        trainContainer to trainContent,
        busContainer to busContent,
        ferryContainer to ferryContent,
        carContainer to carContent
    )

    val builtIn: List<TicketTypeInfo> = listOf(
        TicketTypeInfo("flight", "Самолёт", Icons.Outlined.Flight, flightContainer, flightContent),
        TicketTypeInfo("train", "Поезд", Icons.Outlined.Train, trainContainer, trainContent),
        TicketTypeInfo("bus", "Автобус", Icons.Outlined.DirectionsBus, busContainer, busContent),
        TicketTypeInfo("ferry", "Паром", Icons.Outlined.DirectionsBoat, ferryContainer, ferryContent),
        TicketTypeInfo("car", "Трансфер/авто", Icons.Outlined.DirectionsCar, carContainer, carContent)
    )

    private val builtInByKey = builtIn.associateBy { it.key }

    fun infoFor(typeKey: String): TicketTypeInfo {
        builtInByKey[typeKey]?.let { return it }
        val (container, content) = customPalette[
            (typeKey.hashCode().mod(customPalette.size))
        ]
        return TicketTypeInfo(
            key = typeKey,
            label = typeKey,
            icon = Icons.Outlined.ConfirmationNumber,
            containerColor = container,
            contentColor = content,
            isBuiltIn = false
        )
    }

    fun selectableTypes(existingCustomKeys: List<String>): List<TicketTypeInfo> {
        val customOnly = existingCustomKeys
            .filter { it !in builtInByKey.keys }
            .distinct()
            .map { infoFor(it) }
        return builtIn + customOnly
    }
}
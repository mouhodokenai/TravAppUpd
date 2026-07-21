package com.example.travappupd.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Attractions
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.LocalMovies
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SecurityUpdateGood
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


data class NoteTitleInfo(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color,
    val isBuiltIn: Boolean = true
)

object NoteTitles {

    private val flightContainer = Color(0xFFE3F0FB)
    private val flightContent = Color(0xFF2B6CA3)

    private val expectationsContainer = Color(0xFFEDEAFB)
    private val expectationsContent = Color(0xFF6E5FA8)

    private val impressionsContainer = Color(0xFFFCEFE6)
    private val impressionsContent = Color(0xFFB76E3C)

    private val shoppingContainer = Color(0xFFE3F6EF)
    private val shoppingContent = Color(0xFF2F8F70)

    private val attractionsContainer = Color(0xFFFBEAF0)
    private val attractionsContent = Color(0xFFB04C73)

    private val souvenirsContainer = Color(0xFFFCF3DC)
    private val souvenirsContent = Color(0xFFAF8324)

    private val likeContainer = Color(0xFFE9F3DD)
    private val likeContent = Color(0xFF5E8C2E)

    private val dislikeContainer = Color(0xFFEFEFEC)
    private val dislikeContent = Color(0xFF6B6B66)

    private val customPalette = listOf(
        flightContainer to flightContent,
        expectationsContainer to expectationsContent,
        impressionsContainer to impressionsContent,
        shoppingContainer to shoppingContent,
        attractionsContainer to attractionsContent,
        souvenirsContainer to souvenirsContent,
        likeContainer to likeContent
    )

    val builtIn: List<NoteTitleInfo> = listOf(
        NoteTitleInfo("expectations", "Мои ожидания", Icons.Outlined.AutoAwesome, expectationsContainer, expectationsContent),
        NoteTitleInfo("impressions", "Мои впечатления", Icons.Outlined.AutoAwesome, impressionsContainer, impressionsContent),
        NoteTitleInfo("shopping", "Покупки", Icons.Outlined.ShoppingBag, shoppingContainer, shoppingContent),
        NoteTitleInfo("attractions", "Достопримечательности", Icons.Outlined.Attractions, attractionsContainer, attractionsContent),
        NoteTitleInfo("souvenirs", "Сувениры", Icons.Outlined.Diamond, souvenirsContainer, souvenirsContent),
        NoteTitleInfo("like", "Мне понравилось...", Icons.Outlined.SentimentSatisfied, likeContainer, likeContent),
        NoteTitleInfo("dislike", "Мне не понравилось...", Icons.Outlined.SentimentDissatisfied, dislikeContainer, dislikeContent)
    )

    private val builtInByKey = builtIn.associateBy { it.key }

    fun infoFor(categoryKey: String): NoteTitleInfo {
        builtInByKey[categoryKey]?.let { return it }
        val (container, content) = customPalette[
            (categoryKey.hashCode().mod(customPalette.size))
        ]
        return NoteTitleInfo(
            key = categoryKey,
            label = categoryKey,
            icon = Icons.Outlined.Category,
            containerColor = container,
            contentColor = content,
            isBuiltIn = false
        )
    }

    fun selectableTitles(existingCustomKeys: List<String>): List<NoteTitleInfo> {
        val customOnly = existingCustomKeys
            .filter { it !in builtInByKey.keys }
            .distinct()
            .map { infoFor(it) }
        return builtIn + customOnly
    }
}


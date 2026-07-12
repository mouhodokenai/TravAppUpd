package com.example.travelapp.budget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Category as CategoryOutlined
import androidx.compose.material.icons.outlined.DirectionsBus as DirectionsBusOutlined
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.LocalMovies
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SecurityUpdateGood
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BudgetCategoryInfo(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color,
    val isBuiltIn: Boolean = true
)

object BudgetCategories {

    private val flightContainer = Color(0xFFE3F0FB)
    private val flightContent = Color(0xFF2B6CA3)

    private val hotelContainer = Color(0xFFEDEAFB)
    private val hotelContent = Color(0xFF6E5FA8)

    private val foodContainer = Color(0xFFFCEFE6)
    private val foodContent = Color(0xFFB76E3C)

    private val transportContainer = Color(0xFFE3F6EF)
    private val transportContent = Color(0xFF2F8F70)

    private val entertainmentContainer = Color(0xFFFBEAF0)
    private val entertainmentContent = Color(0xFFB04C73)

    private val shoppingContainer = Color(0xFFFCF3DC)
    private val shoppingContent = Color(0xFFAF8324)

    private val insuranceContainer = Color(0xFFE9F3DD)
    private val insuranceContent = Color(0xFF5E8C2E)

    private val otherContainer = Color(0xFFEFEFEC)
    private val otherContent = Color(0xFF6B6B66)

    private val customPalette = listOf(
        flightContainer to flightContent,
        hotelContainer to hotelContent,
        foodContainer to foodContent,
        transportContainer to transportContent,
        entertainmentContainer to entertainmentContent,
        shoppingContainer to shoppingContent,
        insuranceContainer to insuranceContent
    )

    val builtIn: List<BudgetCategoryInfo> = listOf(
        BudgetCategoryInfo("flight", "Перелёт", Icons.Outlined.Flight, flightContainer, flightContent),
        BudgetCategoryInfo("hotel", "Проживание", Icons.Outlined.Hotel, hotelContainer, hotelContent),
        BudgetCategoryInfo("food", "Питание", Icons.Outlined.Restaurant, foodContainer, foodContent),
        BudgetCategoryInfo("transport", "Транспорт", Icons.Outlined.Clear, transportContainer, transportContent),
        BudgetCategoryInfo("entertainment", "Развлечения", Icons.Outlined.LocalMovies, entertainmentContainer, entertainmentContent),
        BudgetCategoryInfo("shopping", "Покупки", Icons.Outlined.ShoppingBag, shoppingContainer, shoppingContent),
        BudgetCategoryInfo("insurance", "Страховка", Icons.Outlined.SecurityUpdateGood, insuranceContainer, insuranceContent),
        BudgetCategoryInfo("other", "Прочее", Icons.Outlined.Clear, otherContainer, otherContent)
    )

    private val builtInByKey = builtIn.associateBy { it.key }

    fun infoFor(categoryKey: String): BudgetCategoryInfo {
        builtInByKey[categoryKey]?.let { return it }
        val (container, content) = customPalette[
            (categoryKey.hashCode().mod(customPalette.size))
        ]
        return BudgetCategoryInfo(
            key = categoryKey,
            label = categoryKey,
            icon = Icons.Outlined.Category,
            containerColor = container,
            contentColor = content,
            isBuiltIn = false
        )
    }

    fun selectableCategories(existingCustomKeys: List<String>): List<BudgetCategoryInfo> {
        val customOnly = existingCustomKeys
            .filter { it !in builtInByKey.keys }
            .distinct()
            .map { infoFor(it) }
        return builtIn + customOnly
    }
}

val SUPPORTED_CURRENCIES = listOf("₽", "$", "€")

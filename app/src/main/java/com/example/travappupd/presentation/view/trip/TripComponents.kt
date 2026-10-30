package com.example.travappupd.presentation.view.trip

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travappupd.presentation.view.dop.DatePickerModal
import com.example.travappupd.ui.theme.ExtendedTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


fun parseToLocalDate(dateString: String): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
        LocalDate.parse(dateString, formatter)
    } catch (e: Exception) {
        null
    }
}

fun formatLocalDate(date: LocalDate?): String {
    return date?.let {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
        it.format(formatter)
    } ?: "выбрать"
}

@Composable
fun TripPreviewCard(
    tripName: String,
    startDate: LocalDate?,
    endDate: LocalDate?,
    isExpanded: Boolean,
    categories: Int,
    onToggleExpanded: () -> Unit
) {

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
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
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = tripName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text =
                            if (startDate != null && endDate != null)
                                if (ChronoUnit.DAYS.between(startDate, (endDate)) > 0)
                                    "${ChronoUnit.DAYS.between((startDate), (endDate))} дней"
                                else "ошибка при выборе даты"
                            else "0 дней",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { 0.17f * categories },
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
                    text = "$categories из 6 разделов",
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onToggleExpanded() }
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
fun TripInfoSection(
    tripName: String,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onTripNameChange: (String) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    isExpanded: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        //visible = true
        visible = isExpanded
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Название поездки",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        BasicTextField(
                            value = tripName,
                            onValueChange = onTripNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ),
                            cursorBrush = SolidColor(
                                MaterialTheme.colorScheme.primary
                            ),
                            decorationBox = { innerTextField ->

                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    if (tripName.isBlank()) {
                                        Text(
                                            text = "Введите название",
                                            color = ExtendedTheme.colors.textColor,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
            OutlinedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditableDateBlock(
                        title = "Дата начала",
                        date = startDate,
                        onDateClick = onStartDateChange,
                        placeholder = "выбрать"
                    )

                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    EditableDateBlock(
                        title = "Дата окончания",
                        date = endDate,
                        onDateClick = onEndDateChange,
                        placeholder = "выбрать"
                    )
                }
            }
        }
    }
}

@Composable
fun EditableDateBlock(
    title: String,
    date: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    placeholder: String = "выбрать"
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = date?.format(dateFormatter) ?: placeholder,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (date == null) ExtendedTheme.colors.textColor else Color(0xFF46465E)
            )
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            initialDate = date,
            onDateSelected = { selectedDate ->
                onDateClick(selectedDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}




@Composable
fun CategoryCard(
    icon: Painter,
    title: String,
    subtitle: String,
    itemCount: Int = 0,
    color: Color,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { onAdd() }
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
                Icon(painter = icon, contentDescription = null, tint = color)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (itemCount > 0) "Добавлено: $itemCount" else subtitle,
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
fun BottomCreateButton(
    isEdit: Boolean,
    onSave: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        tonalElevation = 8.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Button(
                onClick = {
                    onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExtendedTheme.colors.newTripColor,
                    contentColor = ExtendedTheme.colors.textColor
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    if (isEdit) "Сохранить путешествие"
                    else "Создать путешествие"
                )
            }
        }
    }
}